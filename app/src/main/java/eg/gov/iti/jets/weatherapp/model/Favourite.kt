package eg.gov.iti.jets.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite")
data class Favourite(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var address: String? = null
) {}