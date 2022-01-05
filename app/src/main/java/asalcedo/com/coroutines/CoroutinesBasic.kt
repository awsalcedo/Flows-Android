package asalcedo.com.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce

/****
 * Project: Coroutines
 * From: asalcedo.com.coroutines
 * Created by Alex Salcedo Silva on 3/1/22 at 21:49
 * All rights reserve 2022.
 ***/

fun main() {
    //globalScope()
    //suspendFun()

    newTopic("Constructores de coroutinas")
    //constructorRunBlocking()
    //constructorLaunch()
    //constructorAsync()
    constructorProduce()

    //newTopic("Tipos Datos")
    //job()
    //deferred()
    readLine()
}

fun constructorProduce() = runBlocking {
    newTopic("Produce")
    val names = produceNames()
    names.consumeEach {
        println(it)
    }

}

fun CoroutineScope.produceNames(): ReceiveChannel<String> = produce {
    (1..5).forEach {
        send("name$it")
    }
}

fun deferred() {
    runBlocking {
        newTopic("Deferred")
        val deferred = async {
            startMsg()
            delay(2000)
            println("deferred....")
            endMsg()
            5 * 2
            "hola es mi último valor" // Devuelve el útimo valor
        }
        println("Deferred: $deferred")
        println("Valor del Deferred.await: ${deferred.await()}")

        val result = async {
            5 * 5
        }.await()
        println("Result: $result")
    }
}

fun job() {
    runBlocking {
        newTopic("Job")
        //Job es el ciclo de vida de una coroutina, es un trabajo que puede ser cancelable
        val job = launch {
            startMsg()
            delay(2000)
            println("Job")
            endMsg()
        }

        println("Job: $job")
        println("Job is active: ${job.isActive}")
        println("Job is cancelled: ${job.isCancelled}")
        println("Job is completed: ${job.isCompleted}")

    }
}

fun constructorAsync() {

    //Diseñado recibir un valor o resultado al finalizar la coroutina,
    //La devolución del resultado va hacer en un tipo Deferred
    //Debe ser llamado desde una coroutina o desde una suspend function
    runBlocking {
        newTopic("Async")
        val result = async {
            startMsg()
            delay(2000)
            println("Async")
            endMsg()
            "Este es el resultado"
        }
        println("Result: ${result.await()}")
    }

}

fun constructorLaunch() {

    runBlocking {
        newTopic("Launch")
        //Constructor diseñado para hacer tareas que no necesiten devolver un valor o un resultado,
        //debe ser llamado desde una coroutina o una suspend function
        launch {
            startMsg()
            delay(3000)
            println("Launch")
            endMsg()
        }
    }
}

fun constructorRunBlocking() {
    newTopic("RunBlocking")
    runBlocking {
        startMsg()
        delay(3000)
        println("runBlocking.....")
        endMsg()
    }
}

fun suspendFun() {
    newTopic("Suspend")
    Thread.sleep(2000)
    GlobalScope.launch {
        delay(3000)
    }
}

fun globalScope() {
    newTopic("Global Scope")
    GlobalScope.launch {
        startMsg()
        //delay()
        println("Mi coroutina")
        endMsg()
    }
}

fun endMsg() {
    println("Coroutina ${Thread.currentThread().name}- finalizada")
}

fun startMsg() {
    println("Comenzando coroutina -${Thread.currentThread().name}-")
}


private const val SEPARATOR = "===================="
fun newTopic(topic: String) {
    println("\n$SEPARATOR $topic $SEPARATOR\n")
}
