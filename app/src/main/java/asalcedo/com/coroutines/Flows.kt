package asalcedo.com.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

/****
 * Project: Coroutines
 * From: asalcedo.com.coroutines
 * Created by Alex Salcedo Silva on 4/1/22 at 11:07
 * All rights reserve 2022.
 ***/

fun main() {
    //coldFlow()

    //Cancelar un flow, se lo realiza mediante un job, un flow se cancela automáticamnete con la coroutina que la contiene
    //cancelFlow()

    //También son cold proque no se consumiran los recursos hasta que sean solicitados
    //flowOperators()

    //Operadores terminales
    terminalFlowOperators()
}

//Convertir un flow en una lista
fun terminalFlowOperators() {
    runBlocking {
        newTopic("Operadores Flow terminales")
        newTopic(".....List.....")
        val list = getDataByFlow()//.toList()
        println("List: $list")

        newTopic("......Single.........")
        //Usado para una consulta puntual
        val single = getDataByFlow()//.take(1).single()
        println("Single: $single")

        newTopic(".......First.......")
        val first = getDataByFlow()//.first()
        println("First: $first")

        newTopic(".......Last.......")
        val last = getDataByFlow()//.last()
        println("First: $last")
    }
}

fun flowOperators() {
    runBlocking {
        newTopic("Operadores Flow intermediarios....")
        newTopic("......Map.....")
        getDataByFlow().map {
            //Ventaja del map es que se puede utilizar una función suspendida dentro de su bloque de código
            setFormat(it)
            //Si pongo otro proceso sólo saldrá el resultado del último
            setFormat(convertCelsToFahr(it), "F")
        }/*.collect {
            println(it)
        }
        */

        newTopic("....Filter......")
        getDataByFlow().filter {
            //Permite agregar una condición para saber si el dato es apto o no para ser recolectado
            it < 23
        }.map {
            setFormat(it)
        }/*.collect {
            println(it)
        }*/

        newTopic("....Transform.....")
        // Ideal para aquellos escenarios donde requerimos distribuir la información que estamos emitiendo en más de un canal o cuando
        // necesitamos múltiples procesamientos
        getDataByFlow().transform {
            //La ventaja es que podemos emitir un segundo valor o los que queramos
            emit(setFormat(it))
            emit(setFormat(convertCelsToFahr(it), "F"))
        }/*.collect {
            println(it)
        }*/

        newTopic("......Take.......")
        // Permite limitar el tamaño del flow en este caso a 3
        getDataByFlow().take(3).map {
            setFormat(it)
        }.collect {
            println(it)
        }

    }
}

fun convertCelsToFahr(gradosCelcius: Float): Float = ((gradosCelcius * 9) / 5) + 32

fun setFormat(temperatura: Float, degree: String = "C"): String =
    String.format(Locale.getDefault(), "%.1fº$degree", temperatura)


fun cancelFlow() {
    runBlocking {
        newTopic("Cancelar Flow....")
        val job = launch {
            getDataByFlow().collect {
                println(it)
            }
        }
        delay(6500)
        job.cancel()
    }
}

fun coldFlow() {
    newTopic("Flow are Cold")
    runBlocking {
        val dataFlow = getDataByFlow()
        println("esperando.......")
        delay(2000)
        dataFlow.collect {
            println(it)
        }
    }
}
