package ch.sdp.vibester.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class LyricAPI {
    protected open fun lyricBaseUrl() = "https://api.lyrics.ovh/"

    @Provides
    @Singleton
    fun createLyricService(): LyricsOVHApiInterface {
        return ServiceBuilder.buildService(
            lyricBaseUrl(),
            LyricsOVHApiInterface::class.java
        )
    }
}