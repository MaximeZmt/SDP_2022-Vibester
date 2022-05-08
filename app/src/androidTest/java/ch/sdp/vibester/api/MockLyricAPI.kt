package ch.sdp.vibester.api

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LyricAPI::class]
)
class MockLyricAPI: LyricAPI() {

    override fun lyricBaseUrl(): String {
        return "http://localhost/"
    }
}