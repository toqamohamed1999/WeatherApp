package eg.gov.iti.jets.weatherapp.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.io.Serializable
import java.util.*

@Entity(tableName = "alert", primaryKeys = ["currentTime"])
data class AlertModel(

    var currentTime: String,
    var latitude: String? = null,
    var longitude: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var address: String? = null,
    var alertEnabled : Boolean

) : Serializable {}