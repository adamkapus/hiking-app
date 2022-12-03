package com.adamkapus.hikingapp.domain.interactor.gpx

import android.util.Log
import com.adamkapus.hikingapp.domain.model.map.Coordinate
import io.ticofab.androidgpxparser.parser.GPXParser
import io.ticofab.androidgpxparser.parser.domain.Extensions
import io.ticofab.androidgpxparser.parser.domain.Gpx
import io.ticofab.androidgpxparser.parser.domain.Track
import io.ticofab.androidgpxparser.parser.domain.TrackSegment
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

const val TAG = "GPX"

class GpxInteractor @Inject constructor() {

    //ToDo suspend
    suspend fun parseFile(inputStream: InputStream): MutableList<Coordinate> {
        val route: MutableList<Coordinate> = mutableListOf()
        inputStream.use { inputStream ->
            val mParser = GPXParser()
            var parsedGpx: Gpx? = null
            try {
                parsedGpx = mParser.parse(inputStream) // consider doing this on a background thread
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            }

            if (parsedGpx != null) {
                // log stuff
                val tracks: List<Track> = parsedGpx.tracks
                for (i in tracks.indices) {
                    val track: Track = tracks[i]
                    Log.d(TAG, "track $i:")
                    val segments: List<TrackSegment> = track.getTrackSegments()
                    for (j in segments.indices) {
                        val segment = segments[j]
                        Log.d(TAG, "  segment $j:")
                        for (trackPoint in segment.trackPoints) {
                            route.add(Coordinate(trackPoint.latitude, trackPoint.longitude))
                            var msg =
                                "    point: lat " + trackPoint.latitude + ", lon " + trackPoint.longitude + ", time " + trackPoint.time
                            val ext: Extensions? = trackPoint.extensions
                            var speed: Double
                            if (ext != null) {
                                speed = ext.getSpeed()
                                msg = "$msg, speed $speed"
                            }
                            Log.d(TAG, msg)
                        }
                    }
                }

            } else {
                Log.e(TAG, "Error parsing gpx track!")
            }

        }
        return route
    }
}