package com.example.alejandro.calculadora

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat
import kotlin.system.exitProcess

class CalculadoraActivity : AppCompatActivity() {

    private var opLeft = "0"
    private var opRight = ""
    private var operator = ""
    private var memOp = "0"
    private var result = false
    private lateinit var textoResultado: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora)


        textoResultado = findViewById(R.id.textoResultado)


        if (savedInstanceState != null) {
            // Recuperamos una variable que se almacena con el estado
            opLeft = savedInstanceState.getString("opLeft")
            opRight = savedInstanceState.getString("opRight")
            operator = savedInstanceState.getString("operator")
            memOp = savedInstanceState.getString("memOp")
            result = savedInstanceState.getBoolean("result")
            actualizarPantalla()

        }


        //Si la orientación del dispositivo es horizontal
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            findViewById<Button>(R.id.memWrite).setOnClickListener {
                val ultimo = ultimoNum()
                if(ultimo == "opLeft"){
                    opLeft = memOp
                }else {
                    opRight = memOp
                }

                actualizarPantalla()
            }

            findViewById<Button>(R.id.memClean).setOnClickListener {
                memOp = "0"
            }

            findViewById<Button>(R.id.memLess).setOnClickListener {
                val ultimo = ultimoNum()
                memOp = if(ultimo == "opLeft" || ultimo == "operator"){
                    devolverNumero(memOp.toDouble() - opLeft.toDouble())
                }else {
                    devolverNumero(memOp.toDouble() - opRight.toDouble())
                }
                opLeft = "0"
                actualizarPantalla()
            }

            findViewById<Button>(R.id.poweDouble).setOnClickListener {
                opRight = "2"
                addOperation("^")
                operate()
            }

            findViewById<Button>(R.id.sin).setOnClickListener {
                addOperation("sin")
                operate()
            }

            findViewById<Button>(R.id.cos).setOnClickListener {
                addOperation("cos")
                operate()
            }

            findViewById<Button>(R.id.tan).setOnClickListener {
                addOperation("tan")
                operate()
            }

            findViewById<Button>(R.id.oneDivideX).setOnClickListener {
                opRight = opLeft
                opLeft = "1"
                addOperation("/")
            }

            findViewById<Button>(R.id.pi).setOnClickListener {
                addNumber("3.14")
            }
            //Si la orientación del dispositivo es vertical
        }else{
            findViewById<Button>(R.id.Off).setOnClickListener {
                exitProcess(-1)
            }

            findViewById<Button>(R.id.potencia).setOnClickListener {
                addOperation("^")
            }

            findViewById<Button>(R.id.mem).setOnClickListener {
                val ultimo = ultimoNum()
                if(ultimo == "opLeft"){
                    opLeft = memOp
                }else {
                    opRight = memOp
                }

                actualizarPantalla()
            }

        }

        findViewById<Button>(R.id.memAdd).setOnClickListener {
            val ultimo = ultimoNum()

            memOp = if(ultimo == "opLeft" || ultimo == "operator"){

                devolverNumero(memOp.toDouble() + opLeft.toDouble())
            }else {
                devolverNumero(memOp.toDouble() + opRight.toDouble())
            }
            opLeft = "0"
            actualizarPantalla()
        }

        findViewById<Button>(R.id.Clean).setOnClickListener {
            memOp = "0"
            limpiarPantalla()
        }

        findViewById<Button>(R.id.CleanLast).setOnClickListener {
            val ultimo = ultimoNum()
            if (ultimo == "opLeft" ) {
                opLeft = "0"
            } else if (ultimo == "opRight"|| ultimo == "operator" ) {
                opRight = "0"
            }
            actualizarPantalla()
        }

        findViewById<Button>(R.id.Plus).setOnClickListener {
            addOperation("+")
        }

        findViewById<Button>(R.id.nine).setOnClickListener {
            addNumber("9")
        }

        findViewById<Button>(R.id.eight).setOnClickListener {
            addNumber("8")
        }

        findViewById<Button>(R.id.seven).setOnClickListener {
            addNumber("7")
        }

        findViewById<Button>(R.id.minus).setOnClickListener {
            addOperation("-")
        }

        findViewById<Button>(R.id.six).setOnClickListener {
            addNumber("6")
        }

        findViewById<Button>(R.id.five).setOnClickListener {
            addNumber("5")
        }

        findViewById<Button>(R.id.four).setOnClickListener {
            addNumber("4")
        }

        findViewById<Button>(R.id.raiz).setOnClickListener {
            opRight = "2"
            addOperation("RAIZ")
            operate()
        }

        findViewById<Button>(R.id.multiply).setOnClickListener {
            addOperation("*")
        }

        findViewById<Button>(R.id.three).setOnClickListener {
            addNumber("3")
        }
        findViewById<Button>(R.id.two).setOnClickListener {
            addNumber("2")
        }

        findViewById<Button>(R.id.one).setOnClickListener {
            addNumber("1")
        }

        findViewById<Button>(R.id.mod).setOnClickListener {
            addOperation("%")
        }

        findViewById<Button>(R.id.divide).setOnClickListener {
            addOperation("/")
        }

        findViewById<Button>(R.id.zero).setOnClickListener {
            addNumber("0")
        }

        findViewById<Button>(R.id.point).setOnClickListener {
            addNumber(".")
        }

        findViewById<Button>(R.id.plusminus).setOnClickListener {
            addOperation("+/-")
        }


        findViewById<Button>(R.id.equal).setOnClickListener {
            val ultimo = ultimoNum()
            if (ultimo == "opRight") {
                operate()
                actualizarPantalla()
            } else if (ultimo == "operator") {
                opRight = opLeft
                operate()
                actualizarPantalla()
            }
        }
    }

    override fun onSaveInstanceState(state: Bundle) {

        // Añadimos una variable que se almacena con el estado
        state.putString("opLeft", opLeft)
        state.putString("opRight", opRight)
        state.putString("operator", operator)
        state.putString("memOp", memOp)
        state.putBoolean("result", result)

        // Guardamos el estado de la Actividad (sus componentes)
        super.onSaveInstanceState(state)

    }

    /*
        Función que obtiene el elemento que se va a escribir a continuación
     */
    private fun ultimoNum(): String{
        return if(operator == ""){
            "opLeft"
        }else if (operator != "" && opRight == ""){
            "operator"
        }else{
            "opRight"
        }
    }

    /*
        Función que gestiona como se añade un número a uno de los dos operadores
    */
    private fun addNumber(num:String){
        val ultimo = ultimoNum()

        if (ultimo == "opLeft" && result) {
            limpiarPantalla()
            result = false
        }

        if(ultimo == "opLeft" ){
            //Si es un punto y el operador no tiene uno
            if(opLeft.indexOf('.', 0, false) == -1 && num == ".") {
                if (opLeft == "0") {
                    opLeft = "0."
                } else {
                    opLeft += "."
                }
            }
            //Si no es un punto
            if( num != ".") {
                if (opLeft == "0"){
                    opLeft = ""
                }
                opLeft += num
            }
        }else{

            //Si es un punto y el operador no tiene uno
            if(opRight.indexOf('.', 0, false) == -1 && num == ".") {
                if (opRight == "0" || opRight == "") {
                    opRight = "0."
                } else {
                    opRight += "."
                }
            }
            //Si no es un punto
            if( num != ".") {
                if (opRight == "0"){
                    opRight = ""
                }
                opRight += num
            }
        }

        actualizarPantalla()
    }

    /*
        Función que añade la operación
     */
    private fun addOperation(op:String){
        val ultimo = ultimoNum()

        result = false
        operator = if(ultimo != "opRight"){
            op
        }else{
            operate()
            op

        }
        actualizarPantalla()
    }

    /*
        Función que ejecuta la operación y actualiza los operadores
     */
    private fun operate(){

        val resultado = when (operator) {
            "+" -> (opLeft.toDouble() + opRight.toDouble())
            "-" -> (opLeft.toDouble() - opRight.toDouble())
            "*" -> (opLeft.toDouble() * opRight.toDouble())
            "/" -> (opLeft.toDouble() / opRight.toDouble())
            "^" -> Math.pow(opLeft.toDouble(), opRight.toDouble())
            "RAIZ" -> Math.sqrt(opLeft.toDouble())
            "+/-" -> (opLeft.toDouble() / -1* opRight.toDouble())
            "%" -> ((opLeft.toDouble()/100) * opRight.toDouble())
            "sin" -> (Math.sin(Math.toRadians(opLeft.toDouble())))
            "cos" -> (Math.cos(Math.toRadians(opLeft.toDouble())))
            else -> (Math.tan(Math.toRadians(opLeft.toDouble())))
        }

        opLeft = devolverNumero(resultado)

        textoResultado.text = opLeft
        operator = ""
        opRight = ""
        result = true
    }

    /*
    Función que comprueba si el número es entero o decimal y devuelve en función de ello
    */
    private fun devolverNumero(num: Double): String{
        return if(num % 1 == 0.0){
            num.toInt().toString()
        }else{
            num.toString()
        }
    }

    /*
        Función que actualiza la pantalla cada vez que se pulsa un botón
     */
    private fun actualizarPantalla(){

        val texto = opLeft+operator+opRight
        textoResultado.text = texto
    }

    /*
        Función que limpia la pantalla de los números
     */
    private fun limpiarPantalla(){
        opRight = ""
        operator = ""
        opLeft = "0"
        actualizarPantalla()
    }

}
