import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wezcasting.View.Home.ViewModel.HomeViewModel
import com.example.wezcasting.ViewModel.FakeWeatherRepository
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import org.junit.Before
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapViewModelTest {
    lateinit var repository: FakeWeatherRepository
    lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp(){
        repository = FakeWeatherRepository()
        homeViewModel = HomeViewModel(repository, 15.0, 15.0, "en", "matric")
    }

    @Test
    fun saveLocation_UpdateLatLon() {
        // Given
        val myLat: Double = 23.5
        val myLon: Double = 55.160

        // When
        homeViewModel.lat = myLat
        homeViewModel.lon = myLon

        // Then
        assertThat(homeViewModel.lat, IsEqual(myLat))
        assertThat(homeViewModel.lon, IsEqual(myLon))
    }

    @Test
    fun getCurrentWeather_ReturnsExpectedData() = runTest {
        // When
        homeViewModel.getCurrentWeather()

        // Wait
        val currentWeather = homeViewModel.data.first()

        // Then
        assertEquals("Ahmed", currentWeather?.name)
        currentWeather?.main?.temp?.let { assertEquals(34.4, it, 0.1) }
        assertEquals("c", currentWeather?.tempUnit)
    }

    @Test
    fun getWeatherForecast_ReturnsExpectedData() = runTest {
        // When
        homeViewModel.getWeatherForecast()

        // Wait
        val weatherForecast = homeViewModel.dataForecast.first()

        // Then
        assertEquals("TestCity", weatherForecast?.city?.name)
        weatherForecast?.list?.get(0)?.main?.temp?.let { assertEquals(22.5, it, 0.1) }
        assertEquals("Clear", weatherForecast?.list?.get(0)?.weather?.get(0)?.main)
    }

}