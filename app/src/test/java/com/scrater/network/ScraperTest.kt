package com.scrater.network

import com.google.common.truth.Truth.assertThat
import com.scrater.TestHelpers.Companion.parseResponse
import com.scrater.data.source.remote.Scraper
import org.junit.Test

class ScraperTest {
    @Test
    fun `parse json response`() {
        val response = parseResponse()
        assertThat(response.hasMoreItems).isTrue()
        assertThat(response.htmlContent).isNotEmpty()
    }

    @Test
    fun `parse html`() {
        val tweets = Scraper.parseHtml("elonmusk", parseResponse().htmlContent)
        assertThat(tweets[0].isPinned).isTrue()
        assertThat(tweets[0].isRetweet).isFalse()
        assertThat(tweets[1].isPinned).isFalse()
        assertThat(tweets[1].isRetweet).isTrue()
        assertThat(tweets[8].entries.videos).isEqualTo(1)
        assertThat(tweets[12].entries.photos.size).isEqualTo(4)
    }
}
