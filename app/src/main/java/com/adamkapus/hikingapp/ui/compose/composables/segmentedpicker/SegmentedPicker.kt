package com.adamkapus.hikingapp.ui.compose.composables.segmentedpicker

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.adamkapus.hikingapp.R
import com.adamkapus.hikingapp.ui.compose.theme.hikingAppTypography

@Preview
@Composable
fun SegmentedDemo() {
    Surface(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {

            val twoSegments = remember { listOf("Registration", "Login") }
            var selectedTwoSegment by remember { mutableStateOf(twoSegments.first()) }
            SegmentedPicker(
                segments = twoSegments,
                selectedSegment = selectedTwoSegment,
                onSegmentSelected = { selectedTwoSegment = it },
                backgroundColor = colorResource(id = R.color.dark_blue),
                activeItemBackgroundColor = colorResource(id = R.color.white),
                activeItemTextColor = colorResource(id = R.color.black),
                inactiveItemTextColor = colorResource(id = R.color.white),
            )

            val threeSegments = remember { listOf("Foo", "Bar", "Some very long string") }
            var selectedThreeSegment by remember { mutableStateOf(threeSegments.first()) }
            SegmentedPicker(
                segments = threeSegments,
                selectedSegment = selectedThreeSegment,
                onSegmentSelected = { selectedThreeSegment = it },
                backgroundColor = colorResource(id = R.color.dark_blue),
                activeItemBackgroundColor = colorResource(id = R.color.white),
                activeItemTextColor = colorResource(id = R.color.black),
                inactiveItemTextColor = colorResource(id = R.color.white),
            )
        }
    }
}

@Composable
fun SegmentedPicker(
    segments: List<String>,
    selectedSegment: String,
    onSegmentSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(id = R.color.dark_blue),
    activeItemBackgroundColor: Color = colorResource(id = R.color.white),
    activeItemTextColor: Color = colorResource(id = R.color.black),
    inactiveItemTextColor: Color = colorResource(id = R.color.white)
) {
    val pressedTrackPadding: Dp = dimensionResource(id = R.dimen.segmentedPickerPressedTrackPadding)
    val state = remember { SegmentedControlState(pressedTrackPadding) }
    state.segmentCount = segments.size
    state.selectedSegment = segments.indexOf(selectedSegment)
    state.onSegmentSelected = { onSegmentSelected(segments[it]) }

    // Animate between whole-number indices so we don't need to do pixel calculations.
    val selectedIndexOffset by animateFloatAsState(state.selectedSegment.toFloat())

    // Use a custom layout so that we can measure the thumb using the height of the segments. The thumb
    // is whole composable that draws itself – this layout is just responsible for placing it under
    // the correct segment.
    Layout(
        content = {
            // Each of these produces a single measurable.
            Thumb(state, activeItemBackgroundColor)
            Segments(
                state,
                segments,
                activeItemTextColor = activeItemTextColor,
                inactiveItemTextColor = inactiveItemTextColor
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .then(state.inputModifier)
            .background(
                backgroundColor,
                RoundedCornerShape(dimensionResource(id = R.dimen.segmentedPickerCornerSize))
            )
            .padding(dimensionResource(id = R.dimen.segmentedPickerTrackPadding))
    ) { measurables, constraints ->
        val (thumbMeasurable, segmentsMeasurable) = measurables

        // Measure the segments first so we know how tall to make the thumb.
        val segmentsPlaceable = segmentsMeasurable.measure(constraints)
        state.updatePressedScale(segmentsPlaceable.height, this)

        // Now we can measure the thumb to be the right size.
        val thumbPlaceable = thumbMeasurable.measure(
            Constraints.fixed(
                width = segmentsPlaceable.width / segments.size,
                height = segmentsPlaceable.height
            )
        )

        layout(segmentsPlaceable.width, segmentsPlaceable.height) {
            val segmentWidth = segmentsPlaceable.width / segments.size

            // Place the thumb first since it should be drawn below the segments.
            thumbPlaceable.placeRelative(
                x = (selectedIndexOffset * segmentWidth).toInt(),
                y = 0
            )
            segmentsPlaceable.placeRelative(IntOffset.Zero)
        }
    }
}

/**
 * Draws the thumb (selected indicator) on a [SegmentedPicker] track, underneath the [Segments].
 */
@Composable
private fun Thumb(
    state: SegmentedControlState,
    backgroundColor: Color
) {
    Box(
        Modifier
            .then(
                state.segmentScaleModifier(
                    pressed = state.pressedSegment == state.selectedSegment,
                    segment = state.selectedSegment
                )
            )
            .shadow(
                4.dp,
                RoundedCornerShape(dimensionResource(id = R.dimen.segmentedPickerThumbCornerSize))
            )
            .background(
                backgroundColor,
                RoundedCornerShape(dimensionResource(id = R.dimen.segmentedPickerThumbCornerSize))
            )
    )
}

/**
 * Draws the actual segments in a [SegmentedPicker].
 */
@Composable
private fun Segments(
    state: SegmentedControlState,
    segments: List<String>,
    activeItemTextColor: Color,
    inactiveItemTextColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.Absolute.spacedBy(dimensionResource(id = R.dimen.segmentedPickerTrackPadding)),
        modifier = Modifier
            .fillMaxWidth()
            //40 dp - 2*TRACK_PADDING = 40 - 2*4
            .heightIn(min = 32.dp, max = 32.dp)
            .selectableGroup()
    ) {
        segments.forEachIndexed { i, segment ->
            val isSelected = i == state.selectedSegment
            val isPressed = i == state.pressedSegment

            // Alpha to use to indicate pressed state when unselected segments are pressed.
            val pressedUnselectedAlpha = .6f
            // Unselected presses are represented by fading.
            val alpha by animateFloatAsState(if (!isSelected && isPressed) pressedUnselectedAlpha else 1f)

            // We can't use Modifier.selectable because it does way too much: it does its own input
            // handling and wires into Compose's indicaiton/interaction system, which we don't want because
            // we've got our own indication mechanism.
            val semanticsModifier = Modifier.semantics(mergeDescendants = true) {
                selected = isSelected
                role = Role.Button
                onClick { state.onSegmentSelected(i); true }
                stateDescription = if (isSelected) "Selected" else "Not selected"
            }

            Box(
                Modifier
                    .align(Alignment.CenterVertically)
                    // Divide space evenly between all segments.
                    .weight(1f)
                    .then(semanticsModifier)
                    .padding(dimensionResource(id = R.dimen.segmentedPickerSegmentPadding))
                    // Draw pressed indication when not selected.
                    .alpha(alpha)
                    // Selected presses are represented by scaling.
                    .then(state.segmentScaleModifier(isPressed && isSelected, i))
            ) {
                Text(
                    text = segment,
                    style = MaterialTheme.hikingAppTypography.segmentedPickerTextStyle,
                    modifier = Modifier.align(Alignment.Center),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (isSelected) activeItemTextColor else inactiveItemTextColor,
                )
            }
        }
    }
}

private class SegmentedControlState(val pressedTrackPadding: Dp) {

    var segmentCount by mutableStateOf(0)
    var selectedSegment by mutableStateOf(0)
    var onSegmentSelected: (Int) -> Unit by mutableStateOf({})

    //-1 means no segment is selected
    var pressedSegment by mutableStateOf(-1)

    /**
     * Scale factor that should be used to scale pressed segments (both the segment itself and the
     * thumb). When this scale is applied, exactly PRESSED_TRACK_PADDING will be added around the
     * element's usual size.
     */
    var pressedSelectedScale by mutableStateOf(1f)
        private set

    /**
     * Calculates the scale factor we need to use for pressed segments to get the desired padding.
     */
    fun updatePressedScale(controlHeight: Int, density: Density) {
        with(density) {
            val pressedPadding = pressedTrackPadding * 2
            val pressedHeight = controlHeight - pressedPadding.toPx()
            pressedSelectedScale = pressedHeight / controlHeight
        }
    }

    /**
     * Returns a [Modifier] that will scale an element so that it gets PRESSED_TRACK_PADDING extra
     * padding around it. The scale will be animated.
     *
     * The scale is also performed around either the left or right edge of the element if the [segment]
     * is the first or last segment, respectively. In those cases, the scale will also be translated so
     * that PRESSED_TRACK_PADDING will be added on the left or right edge.
     */
    @SuppressLint("ModifierFactoryExtensionFunction")
    fun segmentScaleModifier(
        pressed: Boolean,
        segment: Int,
    ): Modifier = Modifier.composed {
        val scale by animateFloatAsState(if (pressed) pressedSelectedScale else 1f)
        val xOffset by animateDpAsState(if (pressed) pressedTrackPadding else 0.dp)

        graphicsLayer {
            this.scaleX = scale
            this.scaleY = scale

            // Scales on the ends should gravitate to that edge.
            this.transformOrigin = TransformOrigin(
                pivotFractionX = when (segment) {
                    0 -> 0f
                    segmentCount - 1 -> 1f
                    else -> .5f
                },
                pivotFractionY = .5f
            )

            // But should still move inwards to keep the pressed padding consistent with top and bottom.
            this.translationX = when (segment) {
                0 -> xOffset.toPx()
                segmentCount - 1 -> -xOffset.toPx()
                else -> 0f
            }
        }
    }

    /**
     * A [Modifier] that will listen for touch gestures and update the selected and pressed properties
     * of this state appropriately.
     *
     * Input will be reset if the [segmentCount] changes.
     */
    val inputModifier = Modifier.pointerInput(segmentCount) {
        val segmentWidth = size.width / segmentCount

        // Helper to calculate which segment an event occured in.
        fun segmentIndex(change: PointerInputChange): Int =
            ((change.position.x / size.width.toFloat()) * segmentCount)
                .toInt()
                .coerceIn(0, segmentCount - 1)

        forEachGesture {
            awaitPointerEventScope {
                val down = awaitFirstDown()

                pressedSegment = segmentIndex(down)
                val downOnSelected = pressedSegment == selectedSegment
                val segmentBounds = Rect(
                    left = pressedSegment * segmentWidth.toFloat(),
                    right = (pressedSegment + 1) * segmentWidth.toFloat(),
                    top = 0f,
                    bottom = size.height.toFloat()
                )

                // Now that the pointer is down, the rest of the gesture depends on whether the segment that
                // was "pressed" was selected.
                if (downOnSelected) {
                    // When the selected segment is pressed, it can be dragged to other segments to animate the
                    // thumb moving and the segments scaling.
                    horizontalDrag(down.id) { change ->
                        pressedSegment = segmentIndex(change)

                        // Notify the SegmentedControl caller when the pointer changes segments.
                        if (pressedSegment != selectedSegment) {
                            onSegmentSelected(pressedSegment)
                        }
                    }
                } else {
                    // When an unselected segment is pressed, we just animate the alpha of the segment while
                    // the pointer is down. No dragging is supported.
                    waitForUpOrCancellation(inBounds = segmentBounds)
                        // Null means the gesture was cancelled (e.g. dragged out of bounds).
                        ?.let { onSegmentSelected(pressedSegment) }
                }

                // In either case, once the gesture is cancelled, stop showing the pressed indication.
                pressedSegment = -1
            }
        }
    }
}

/**
 * Copy of nullary waitForUpOrCancellation that works with bounds that may not be at 0,0.
 */
private suspend fun AwaitPointerEventScope.waitForUpOrCancellation(inBounds: Rect): PointerInputChange? {
    while (true) {
        val event = awaitPointerEvent(PointerEventPass.Main)
        if (event.changes.all { it.changedToUp() }) {
            // All pointers are up
            return event.changes[0]
        }

        if (event.changes.any { it.consumed.downChange || !inBounds.contains(it.position) }) {
            return null // Canceled
        }

        // Check for cancel by position consumption. We can look on the Final pass of the
        // existing pointer event because it comes after the Main pass we checked above.
        val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
        if (consumeCheck.changes.any { it.positionChangeConsumed() }) {
            return null
        }
    }
}