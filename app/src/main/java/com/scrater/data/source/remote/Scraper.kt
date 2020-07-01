package com.scrater.data.source.remote

import com.scrater.vo.Entries
import com.scrater.vo.Tweet
import com.scrater.vo.Tweeter
import org.jsoup.Jsoup

class Scraper {

    companion object {
        fun scrapeProfile(username:String, htmlContent: String): Tweeter {
            val parsedDocument = Jsoup.parse(htmlContent)
            val avatar = parsedDocument.select(".ProfileAvatar-image")[0]
                .attr("src")
            val banner = parsedDocument.select(".ProfileCanopy-headerBg img")[0]
                .attr("src")
            val pageTitle = parsedDocument.select("title")[0].text()
            val name = pageTitle.substring(0, pageTitle.indexOf("(") - 1)
            val bio = parsedDocument.select(".ProfileHeaderCard-bio")[0].text()
            val website = parsedDocument.select(".ProfileHeaderCard-urlText")[0].text()

            return Tweeter(
                username = username,
                name = name,
                bio = bio,
                website = website,
                avatar = avatar,
                banner = if (banner.isEmpty()) null else banner
            )
        }

        fun scrapeTweets(account: String, htmlContent: String): List<Tweet> {
            val parsedDocument = Jsoup.parse(htmlContent)
            val tweets = parsedDocument.select(".stream-item")
            val profiles = parsedDocument.select(".js-profile-popup-actionable")

            var index = 1
            return (tweets zip profiles)
                .filter {
                    // filter out dummy tweet elements
                    val htmlText = it.first.getElementsByClass("tweet-text").html()
                    !htmlText.isNullOrEmpty()
                }.map { item ->
                    val htmlText = item.first.getElementsByClass("tweet-text").html()

                    val tweetId = item.first.attr("data-item-id")
                    val tweetUrl = item.second.attr("data-permalink-path")
                    val username = item.second.attr("data-screen-name")
                    val avatar = item.first.getElementsByClass("js-action-profile-avatar")
                        .attr("src")
                    val isPinned = item.first.hasClass("js-pinned")
                    val time = item.first.getElementsByClass("tweet-timestamp")
                        .attr("title")

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

                    Tweet(
                        account = account,
                        position = index++,
                        tweetId = tweetId,
                        tweetUrl = tweetUrl,
                        username = username,
                        avatar = avatar,
                        time = time,
                        htmlText = htmlText,
                        replies = replies,
                        retweets = retweets,
                        likes = likes,
                        isRetweet = isRetweet,
                        isPinned = isPinned,
                        entries = Entries(photos, videos)
                    )
                }
        }
    }
}