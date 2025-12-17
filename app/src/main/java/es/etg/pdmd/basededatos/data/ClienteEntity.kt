package es.etg.pdmd.basededatos.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "cliente")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    var nombre:String = "",
    var apellidos:String = "",
    var vip:Boolean = false
)