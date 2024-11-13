import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wezcasting.Model.*
import com.example.wezcasting.Model.interfaces.WeatherLocalDataSourceImp
import com.example.wezcasting.db.WeatherDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var localDataSource: WeatherLocalDataSourceImp

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        weatherDatabase = Room.inMemoryDatabaseBuilder(
            context,
            WeatherDatabase::class.java
        ).allowMainThreadQueries().build()

        localDataSource = WeatherLocalDataSourceImp(weatherDatabase)
    }

    @After
    fun tearDown() {
        weatherDatabase.close()
    }

    @Test
    fun upsertWeather_savesWeatherCorrectly() = runBlocking {
        val testWeather = createTestCurrentWeather(id = 1)
        localDataSource.upsert(testWeather)

        val allWeather = localDataSource.getAllCurrentWeather().first()
        assertEquals(1, allWeather.size)
        assertEquals(testWeather, allWeather.first())
    }

    @Test
    fun removeWeather_removesWeatherSuccessfully() = runBlocking {
        val testWeather = createTestCurrentWeather(id = 1)
        localDataSource.upsert(testWeather)
        localDataSource.remove(testWeather)

        val allWeather = localDataSource.getAllCurrentWeather().first()
        assertTrue(allWeather.isEmpty())
    }

    @Test
    fun removeAllWeather_clearsAllWeatherEntries() = runBlocking {
        val weather1 = createTestCurrentWeather(id = 1)
        val weather2 = createTestCurrentWeather(id = 2)
        localDataSource.upsert(weather1)
        localDataSource.upsert(weather2)

        localDataSource.removeAll()

        val allWeather = localDataSource.getAllCurrentWeather().first()
        assertTrue(allWeather.isEmpty())
    }

    // Helper function to create a sample CurrentWeather object with nested fields
    private fun createTestCurrentWeather(id: Int): CurrentWeather {
        return CurrentWeather(
            id = id,
            tempUnit = "c",
            coord = Coord(lon = -0.1257, lat = 51.5085),
            weather = listOf(
                WeatherCurrent(id = 800, main = "Clear", description = "clear sky", icon = "01d")
            ),
            main = Main(
                temp = 15.0,
                feels_like = 14.5,
                temp_min = 10.0,
                temp_max = 20.0,
                pressure = 1012,
                humidity = 60,
                sea_level = 1012,
                grnd_level = 1008
            ),
            visibility = 10000,
            wind = Wind(speed = 4.1, deg = 80, gust = 6.5),
            clouds = Clouds(all = 5),
            sys = Sys(country = "GB", sunrise = 1603106907, sunset = 1603145907),
            name = "London"
        )
    }
}
