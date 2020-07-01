package com.scrater.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.scrater.TestHelpers.Companion.parseResponse1
import com.scrater.TestHelpers.Companion.readProfileResponse
import com.scrater.vo.Tweet
import org.junit.Test

class ScraperTest {

    @Test
    fun `scrape tweets`() {
        val tweets = Scraper.scrapeTweets("elonmusk", parseResponse1().htmlContent)
        assertThat(tweets[0].isPinned).isTrue()
        assertThat(tweets[0].isRetweet).isFalse()
        assertThat(tweets[0].tweetId).isEqualTo("1263974008900382721")
        assertThat(tweets[0].tweetUrl).isEqualTo("/elonmusk/status/1263974008900382721")
        assertThat(tweets[0].username).isEqualTo("elonmusk")
        assertThat(tweets[0].avatar).isEqualTo("https://pbs.twimg.com/profile_images/1259649222485241858/SO3NRgvJ_bigger.jpg")
        assertThat(tweets[0].time).isEqualTo("4:24 PM - 22 May 2020")
        assertThat(tweets[0].replies).isEqualTo("25,958 replies")
        assertThat(tweets[0].retweets).isEqualTo("90,178 retweets")
        assertThat(tweets[0].likes).isEqualTo("632,735 likes")

        assertThat(tweets[1].isPinned).isFalse()
        assertThat(tweets[1].isRetweet).isTrue()
        assertThat(tweets[8].entries.videos).isEqualTo(1)
        assertThat(tweets[12].entries.photos.size).isEqualTo(4)

        assertThat(tweets).isInStrictOrder { o1, o2 ->
            (o1 as Tweet).position
                .compareTo((o2 as Tweet).position)
        }
    }

    @Test
    fun `scrape profile`() {
        var profile = Scraper.scrapeProfile("spacex", readProfileResponse(1))
        assertThat(profile.avatar).isEqualTo("https://pbs.twimg.com/profile_images/1082744382585856001/rH_k3PtQ_400x400.jpg")
        assertThat(profile.banner).isEqualTo("https://pbs.twimg.com/profile_banners/34743251/1591316434/1500x500")
        assertThat(profile.name).isEqualTo("SpaceX")
        assertThat(profile.bio).isEqualTo("SpaceX designs, manufactures and launches the world’s most advanced rockets and spacecraft")
        assertThat(profile.website).isEqualTo("spacex.com")

        profile = Scraper.scrapeProfile("kotlin", readProfileResponse(2))
        assertThat(profile.banner).isNull()
    }
}
