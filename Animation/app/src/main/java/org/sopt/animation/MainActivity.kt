import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.sopt.animation.R

enum class BoxState { Collapsed, Expanded }

@Composable
fun AnimatedBoxSample() {
    var boxState by remember { mutableStateOf(BoxState.Collapsed) }

    val transition = updateTransition(targetState = boxState, label = "box transition")

    val size by transition.animateDp(label = "size") { state ->
        when (state) {
            BoxState.Collapsed -> 100.dp
            BoxState.Expanded -> 200.dp
        }
    }


    val color by transition.animateColor(label = "color") { state ->
        when (state) {
            BoxState.Collapsed -> Color.Blue
            BoxState.Expanded -> Color.Red
        }
    }

    val subcolor by transition.animateColor(label = "subcolor"){ state ->
        when (state){
            BoxState.Collapsed -> Color.Green
            BoxState.Expanded -> Color.Magenta
        }
    }

    val rotation by transition.animateFloat(label = "rotation") { state ->
        when (state) {
            BoxState.Collapsed -> 0f
            BoxState.Expanded -> 45f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(100.dp),
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .rotate(rotation)
                    .background(color)
                    .clickable {
                        boxState = if (boxState == BoxState.Collapsed) BoxState.Expanded else BoxState.Collapsed
                    }
            )

            Box(
                modifier = Modifier
                    .size(size)
                    .rotate(rotation)
                    .background(subcolor)
                    .clickable {
                        boxState = if (boxState == BoxState.Collapsed) BoxState.Expanded else BoxState.Collapsed
                    }
            )
        }



    }
}



@Preview(showBackground = true)
@Composable
fun AnimatedBoxPreview() {
    AnimatedBoxSample()
}
