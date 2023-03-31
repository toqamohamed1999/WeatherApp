package eg.gov.iti.jets.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alert")
data class AlertModel(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var startDate: String? = null,
    var endDate: String? = null

    ) {}