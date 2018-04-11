package io.github.ReadyMadeProgrammer.Spikot

data class Version(val first: Int,val second:Int,val third:Int){
    override fun toString() = "$first.$second.$third"
    operator fun compareTo(other: Version)
        =(first-other.first)*1000000+(second-other.second)*1000+(third-other.third)
}