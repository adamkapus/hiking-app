package com.adamkapus.hikingapp.data.gpx

import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.domain.model.map.Coordinate
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Gpx
import io.ticofab.androidgpxparser.parser.domain.Track
import io.ticofab.androidgpxparser.parser.domain.TrackSegment
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class GpxDataSource @Inject constructor(
    private val parser: GPXParser
) {

    suspend fun parseGpxFile(inputStream: InputStream): DataSourceResponse<MutableList<Coordinate>> {
        val route: MutableList<Coordinate> = mutableListOf()
        inputStream.use { stream ->
            var parsedGpx: Gpx? = null
            try {
                parsedGpx = parser.parse(stream)
            } catch (e: IOException) {
                return DataSourceError
            } catch (e: XmlPullParserException) {
                return DataSourceError
            }

            if (parsedGpx != null) {
                val tracks: List<Track> = parsedGpx.tracks
                for (i in tracks.indices) {
                    val track: Track = tracks[i]
                    val segments: List<TrackSegment> = track.getTrackSegments()
                    for (j in segments.indices) {
                        val segment = segments[j]
                        for (trackPoint in segment.trackPoints) {
                            route.add(Coordinate(trackPoint.latitude, trackPoint.longitude))
                        }
                    }
                }

            } else {
                return DataSourceError
            }

        }
        return DataSourceResult(route)
    }

}