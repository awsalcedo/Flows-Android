package asalcedo.com.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

/****
 * Project: Coroutines
 * From: asalcedo.com.coroutines
 * Created by Alex Salcedo Silva on 3/1/22 at 23:50
 * All rights reserve 2022.
 ***/


fun main() {
    //dispatchers()

    //Anidamiento de coroutinas, si un hijo se cancela automáticamente el padre podría cancelarse y viceversa
    //Si tenemos varias corrutinas anidadas, y existe un error dentro de una corrutina "hija", ¿Qué pasa con el resto de los procesos? Sólo
    // la coroutina hija con la excepción se cancela, el resto sigue funcionando
    //nested()

    //Cambiar el contexto de ejecución de la coroutina
    //¿Para qué sirve "withContext"? Para cambiar de contexto y ejecutar un subproceso en un hilo diferente al del padre, sin cambiar de coroutina
    //changeWithContext()

    //Secuencias son un tipo de dato que es un una colección que se enfoca en procesar y entregar valores por pasos,
    //tiene la característica de ejecutar el procesamiento por cada elemento y de catalogarse como perezosa, es decir que
    //procesará cada elemento hasta que se solicite y no toda la colección en una sola acción
    //para devolver un dato se usa un método específico yield()

    //sequences()
    basicFlows()
}

fun basicFlows() {
    newTopic("Flows básicos")
    runBlocking {
        launch {
            getDataByFlow().collect {
                println(it)
            }
        }
        launch {
            (1..50).forEach {
                delay(150)
                println("Tarea 2.....")

            }
        }
    }
}

// Flow es usado cuando se tiene código asíncrono que retorna múltiples valores
fun getDataByFlow(): Flow<Float> {
    return flow {
        (1..5).forEach {
            println("Procesando datos...")
            delay(2000)
            //Con yield devolvemos datos, en este caso al método forEach() del método getDatabySec()
            emit(20 + it + Random.nextFloat())
        }
    }
}

fun sequences() {
    newTopic("Sequences")
    getDataBySec().forEach {
        println("${it}º")
    }
}

fun getDataBySec(): Sequence<Float> {
    return sequence {
        (1..5).forEach {
            println("Procesando datos...")
            Thread.sleep(2000)
            //Con yield devolvemos datos, en este caso al método forEach() del método getDatabySec()
            yield(20 + it + Random.nextFloat())
        }
    }
}

fun changeWithContext() {
    runBlocking {
        newTopic("WithContext")
        startMsg()
        //Cambiar el contexto del padre
        withContext(newSingleThreadContext("Curso Coroutine")) {
            startMsg()
            delay(2500)
            println("Curso Android Coroutine")
            endMsg()
        }

        withContext(Dispatchers.IO) {
            startMsg()
            delay(2500)
            println("Petición al servidor")
            endMsg()
        }
        endMsg()
    }
}

fun nested() {
    runBlocking {
        newTopic("Coroutines anidadas")
        val job = launch {
            startMsg()

            //Crear otra coroutina
            launch {
                startMsg()
                delay(2500)
                println("Otra tarea")
                endMsg()
            }

            val jobIO = launch(Dispatchers.IO) {
                startMsg()

                launch(newSingleThreadContext("Coroutina persnalizada anidada")) {
                    startMsg()
                    println("Tarea personalizada anidada")
                    endMsg()
                }

                delay(2000)
                println("Tarea en el servidor")
                endMsg()
            }

            delay(350)
            jobIO.cancel()
            println("jobIO cancelado.....")


            var sum = 0
            (1..100).forEach {
                sum += it
                delay(300)
            }
            println("Sum= $sum")
            endMsg()
        }

        delay(800)
        job.cancel()
        println("Job cancelado......")
    }
}

fun dispatchers() {
    //Los dispatchers sirve para definir donde queremos ejecutar los hilos de las coroutines
    runBlocking {
        newTopic("Dispatchers")
        launch {
            startMsg()
            println("No definido")
            endMsg()
        }
        launch(Dispatchers.IO) {
            startMsg()
            println("IO: ideal para conexiones a BDD locales o remotas, lectura y escritura de archivos y cualquier tarea de larga duración")
            endMsg()
        }

        launch(Dispatchers.Unconfined) {
            startMsg()
            println(
                "Unconfined: recomendado para procesos en donde no se requiere compartir datos con otras coroutinas, " +
                        "también podemos cambiar el hilo si es que se encuentra en una función suspendida," +
                        "es el menos comun que ni siquiera aparece en la documentación oficiale de Android con coroutinas"
            )
            endMsg()
        }
        launch(Dispatchers.Default) {
            startMsg()
            println(
                "Default: recomendable para tareas que tengan un uso intensivo de la CPU, cálculos complejos, procesamiento " +
                        "de imagenes o cualquier tarea que no encaje en IO"
            )
            endMsg()
        }
        launch(newSingleThreadContext("Curso Android Coroutines")) {
            startMsg()
            println("Mi coroutina Personalizada con un dispatcher: recomendado para la depuración")
            endMsg()
        }
        newSingleThreadContext("CursoCoroutine").use { myContext ->
            launch(myContext) {
                startMsg()
                println("Mi coroutina personalizada con un dispatcher 2: recomendado para la depuración")
                endMsg()
            }

        }
        //Main es usado solo para Android, es decir da error en este tipo de archivo
        /*launch(Dispatchers.Main) {
            startMsg()
            println("Es el hilo principal conectado a la interfaz del usuario, sólo es recomendable usarlo para tareas muy rápidas o" +
                    "que estan relacionadas con el cambio en la UI")
            endMsg()
        }
         */
    }
}
