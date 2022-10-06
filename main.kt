import kotlin.math.abs

const val whitePawn = "W"
const val blackPawn = "B"
var turn = 0
val alph = mutableListOf<String>("a", "b", "c", "d", "e", "f", "g", "h")
var lastWhiteStop = ""
var lastBlackStop = ""
val regex = Regex("[a-hA-H][1-8][a-hA-H][1-8]")
var chessField = MutableList(8) {MutableList(8) {" "}}
var win = ""
var whites = 8
var blacks = 8

fun startGame() {
    for (i in 0..7) {
        chessField[1][i] = whitePawn
        chessField[6][i] = blackPawn
    }
}

fun checkStalemate(): Boolean {
    var check = true
    // check whites
    if (turn == 1) {
        for (h in chessField.indices) {
            for (v in chessField.indices) {
                if (chessField[h][v] == "B") {
                    if (chessField[h - 1][v] != "W") {
                        check = false
                    }
                }
            }
        }
    } else if (turn == 0) {
        for (h in chessField.indices) {
            for (v in chessField.indices) {
                if (chessField[h][v] == "W") {
                    if (chessField[h - 1][v] != "B") {
                        check = false
                    }
                }
            }
        }
    }

    return check

}

fun changePlayer() {
    turn = if (turn == 0) 1 else 0
}

fun movePawn(coordinates: String) {
    var playerPawn = if (turn == 0) whitePawn else blackPawn
    var startH = alph.indexOf(coordinates.first().toString())
    var startV = coordinates[1].toString().toInt()-1
    var stopH = alph.indexOf(coordinates[2].toString())
    var stopV = coordinates.last().toString().toInt()-1

    if (chessField[startV][startH] == playerPawn) {
        if (startH != stopH) {
            if (turn == 0 && abs(startH - stopH) == 1 && abs(startV - stopV) == 1 && (chessField[stopV][stopH] == blackPawn || chessField[stopV - 1][stopH] == blackPawn)) {
                if (chessField[stopV - 1][stopH] == blackPawn) {
                    if ("$startV$startH" != lastWhiteStop) {
                        println("Invalid Input")
                        return
                    }
                    chessField[stopV - 1][stopH] = " "
                }
                chessField[stopV][stopH] = whitePawn
                chessField[startV][startH] = " "
                lastWhiteStop = "$stopV$stopH"
                blacks--
                displayField()
                changePlayer()
                if (stopV == 0) {
                    win = "White"
                }
                return
            } else if (turn == 1 && abs(startH - stopH) == 1 && abs(startV - stopV) == 1 && (chessField[stopV][stopH] == whitePawn || chessField[stopV + 1][stopH] == whitePawn)) {
                if (chessField[stopV + 1][stopH] == whitePawn) {
                    if ("$startV$startH" != lastBlackStop) {
                        println("Invalid Input")
                        return
                    }
                    chessField[stopV + 1][stopH] = " "
                }
                chessField[stopV][stopH] = blackPawn
                chessField[startV][startH] = " "
                lastBlackStop = "$stopV$stopH"
                displayField()
                changePlayer()
                whites--
                if (stopV == 7) {
                    win = "Black"
                }
                return
            } else {
                println("Invalid Input")
                return
            }
        }
        if (turn == 0 && startV == 1 && stopV - startV > 2 || turn == 0 && startV != 1 && stopV - startV > 1 || turn == 0 && startV >= stopV) {
            println("Invalid Input")
            return
        }
        if (turn == 1 && startV == 6 && startV - stopV > 2 || turn == 1 && startV != 6 && startV - stopV > 1 || turn == 1 && startV <= stopV) {
            println("Invalid Input")
            return
        }
        if (chessField[stopV][stopH] == " ") {
            chessField[startV][startH] = " "
            chessField[stopV][stopH] = playerPawn

            if (turn == 0) {
                lastWhiteStop = "$stopV$stopH"
                if (stopV == 7) {
                    win = "White"
                }
            } else {
                lastBlackStop = "$stopV$stopH"
                if (stopV == 0) {
                    win = "Black"
                }
            }
            changePlayer()
            displayField()

        } else {
            println("Invalid Input")
        }
    } else {
        println("No ${if (playerPawn == "W") "white" else "black"} pawn at ${coordinates.substring(0,2)}")
    }
}

fun displayField() {
    val line = "  +---+---+---+---+---+---+---+---+"
    val endLine = "    a   b   c   d   e   f   g   h"
    println("$line")
    for (i in 7 downTo 0) {
        print("${i+1} |")
        for(j in 0..7) {
            print(" ${chessField[i][j]} |")
        }
        print('\n')
        println(line)
    }
    println(endLine)
}

fun main() {
    // write your code here
    startGame()
    println(" Pawns-Only Chess")
    println("First Player's name:")
    val firstPlayer = readln()
    println("Second Player's name:")
    val secondPlayer = readln()
    displayField()

    while (true) {

        if (!win.isBlank()) {
            println("$win Wins!")
            break
        }
        if (whites == 0) {
            println("Black Wins!")
            break
        } else if (blacks == 0) {
            println("White Wins!")
            break
        }

        if (checkStalemate()) {
            println("Stalemate!")
            break
        }

        print(if (turn == 0) "$firstPlayer's turn:" else "$secondPlayer's turn:")
        var turnCoordinates = readln()
        if (turnCoordinates == "exit") {
            break
        } else if (!regex.matches(turnCoordinates)) {
            println("Invalid Input")
        } else {
            movePawn(coordinates=turnCoordinates)
        }
    }
    println("Bye!")

}