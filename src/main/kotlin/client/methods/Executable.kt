package client.methods

interface Executable <R, T>{
    fun execute(r: R): T
}