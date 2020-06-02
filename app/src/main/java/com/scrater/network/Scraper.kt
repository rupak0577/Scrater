package com.scrater.network

import com.scrater.vo.Entries
import com.scrater.vo.Tweet
import org.jsoup.Jsoup

object Scraper {

    fun parseHtml(htmlContent: String): List<Tweet> {
        val parsedDocument = Jsoup.parse(htmlContent)
        val tweets = parsedDocument.select(".stream-item")
        val profiles = parsedDocument.select(".js-profile-popup-actionable")

        val parsedTweets = mutableListOf<Tweet>()
        for (item in tweets zip profiles) {
            val htmlText = item.first.getElementsByClass("tweet-text").html()
            if (htmlText.isNullOrEmpty())
                continue

            val tweetId = item.first.attr("data-item-id")
            val tweetUrl = item.second.attr("data-permalink-path")
            val username = item.second.attr("data-screen-name")
            val isPinned = item.first.hasClass("js-pinned")
            val timeInMillis = item.first.getElementsByClass("_timestamp")
                .attr("data-time-ms").toLong()

            val interactions = item.first.getElementsByClass("ProfileTweet-actionCount")
                .map { it.text() }.filter { it.isNotEmpty() }
            val replies = interactions[0]
            val retweets = interactions[1]
            val likes = interactions[2]

            val isRetweet = item.first.getElementsByClass("js-stream-tweet")
                .hasAttr("data-retweet-id")

            val photos = item.first.getElementsByClass("AdaptiveMedia-photoContainer")
                .eachAttr("data-image-url")

            val videos = item.first.getElementsByClass("PlayableMedia-player").size

            parsedTweets.add(
                Tweet(
                    tweetId = tweetId,
                    tweetUrl = tweetUrl,
                    username = username,
                    time = timeInMillis,
                    htmlText = htmlText,
                    replies = replies,
                    retweets = retweets,
                    likes = likes,
                    isRetweet = isRetweet,
                    isPinned = isPinned,
                    entries = Entries(photos, videos)
                )
            )
        }
        return parsedTweets
    }
}