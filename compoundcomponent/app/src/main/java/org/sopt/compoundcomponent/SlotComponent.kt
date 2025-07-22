package org.sopt.compoundcomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// =============================================================================
// 1. SLOT 패턴 - 외부에서 컴포저블을 주입받는 방식
// =============================================================================

@Composable
fun SlotProfileCard(
    user: User,
    headerContent: @Composable (User) -> Unit,
    bodyContent: @Composable (User) -> Unit,
    footerContent: @Composable (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            headerContent(user)  // 헤더는 외부에서 결정
            bodyContent(user)    // 바디도 외부에서 결정
            footerContent(user)  // 푸터도 외부에서 결정
        }
    }
}

// =============================================================================
// 2. COMPOUND COMPONENT 패턴 - 자식 컴포넌트들이 독립적으로 구성
// =============================================================================

@Composable
fun CompoundProfileCard(
    modifier: Modifier = Modifier,
    content: @Composable CompoundProfileCardScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 스코프를 제공하여 자식 컴포넌트들이 접근할 수 있게 함
            CompoundProfileCardScope().content()
        }
    }
}

// Compound Component의 스코프 클래스
class CompoundProfileCardScope {

    @Composable
    fun Header(
        user: User,
        showStatus: Boolean = true,
        statusColor: Color = Color.Green
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, CircleShape)
                    .padding(8.dp)
            )

            Column {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (showStatus) {
                    Text(
                        text = "● 온라인",
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor
                    )
                }
            }
        }
    }

    @Composable
    fun Body(
        user: User,
        showBio: Boolean = true
    ) {
        Column {
            Text(
                text = "사용자 정보",
                style = MaterialTheme.typography.bodyMedium
            )
            if (showBio) {
                Text(
                    text = user.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }

    @Composable
    fun Actions(
        user: User,
        onFollowClick: () -> Unit = {},
        onMessageClick: () -> Unit = {},
        showFollowButton: Boolean = true,
        showMessageButton: Boolean = true
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showFollowButton) {
                Button(
                    onClick = onFollowClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("팔로우")
                }
            }

            if (showMessageButton) {
                Button(
                    onClick = onMessageClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("메시지")
                }
            }
        }
    }

    @Composable
    fun Stats(
        user: User,
        showFollowers: Boolean = true,
        showPosts: Boolean = true
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (showPosts) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${user.postsCount}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "게시물",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            if (showFollowers) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${user.followersCount}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "팔로워",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// =============================================================================
// 데이터 클래스
// =============================================================================

data class User(
    val name: String,
    val bio: String = "안녕하세요! 반갑습니다.",
    val postsCount: Int = 42,
    val followersCount: Int = 1234,
    val isOnline: Boolean = true,
    val isFollowing: Boolean = false
)

// =============================================================================
// 미리보기 - SLOT 패턴 사용 예시들
// =============================================================================

@Preview(name = "Slot Pattern - 기본 구성", showBackground = true)
@Composable
private fun PreviewSlotBasic() {
    val user = User(name = "홍길동")

    SlotProfileCard(
        user = user,
        headerContent = { user ->
            // 헤더를 외부에서 직접 구성
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = user.name, style = MaterialTheme.typography.titleLarge)
            }
        },
        bodyContent = { user ->
            // 바디를 외부에서 직접 구성
            Text(
                text = user.bio,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        footerContent = { user ->
            // 푸터를 외부에서 직접 구성
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("팔로우")
            }
        }
    )
}

@Preview(name = "Slot Pattern - 커스텀 구성", showBackground = true)
@Composable
private fun PreviewSlotCustom() {
    val user = User(name = "김철수")

    SlotProfileCard(
        user = user,
        headerContent = { user ->
            // 완전히 다른 헤더 구성
            Column {
                Text(
                    text = "🌟 VIP 사용자",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Yellow
                )
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        bodyContent = { user ->
            // 통계 정보를 바디에 표시
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${user.postsCount}")
                    Text(text = "Posts", color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${user.followersCount}")
                    Text(text = "Followers", color = Color.Gray)
                }
            }
        },
        footerContent = { user ->
            // 여러 버튼을 푸터에 배치
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Favorite, null)
                }
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    Text("메시지")
                }
                Button(onClick = {}, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Settings, null)
                }
            }
        }
    )
}

// =============================================================================
// 미리보기 - COMPOUND COMPONENT 패턴 사용 예시들
// =============================================================================

@Preview(name = "Compound - 기본 구성", showBackground = true)
@Composable
private fun PreviewCompoundBasic() {
    val user = User(name = "이영희")

    CompoundProfileCard {
        Header(user = user)
        Body(user = user)
        Actions(user = user)
    }
}

@Preview(name = "Compound - 선택적 구성", showBackground = true)
@Composable
private fun PreviewCompoundSelective() {
    val user = User(name = "박민수")

    CompoundProfileCard {
        Header(
            user = user,
            showStatus = false  // 상태 숨기기
        )
        Stats(user = user)  // 통계 정보만 표시
        Actions(
            user = user,
            showMessageButton = false  // 메시지 버튼 숨기기
        )
        // Body는 생략
    }
}

@Preview(name = "Compound - 유연한 조합", showBackground = true)
@Composable
private fun PreviewCompoundFlexible() {
    val user = User(name = "최지은")

    CompoundProfileCard {
        Header(
            user = user,
            statusColor = Color.Red
        )

        // 중간에 커스텀 컴포넌트 삽입 가능
        Text(
            text = "🎉 오늘의 추천 사용자!",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Blue.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                .padding(8.dp)
        )

        Body(user = user, showBio = false)
        Stats(user = user)
        Actions(user = user)
    }
}