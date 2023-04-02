package eg.gov.iti.jets.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "alert")
data class AlertModel(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var address: String? = null

) : Serializable {}