package com.wan.noveldownloader.common

import android.content.Context
import com.wan.noveldownloader.model.DownloadingItem
import org.jsoup.Jsoup
import java.io.File
import java.text.DecimalFormat


/**
 *
 * @author StarsOne
 * @date Create in  2019/10/5 0005 17:03
 * @description
 *
 */
class NovelDownloadTool(var url: String,var itemPosition: Int) {

    var chacterMap = hashMapOf<Int, String>()
    private var name = ""
    private var imgUrl = ""
    private var tempFileHead = ""

    fun getMessage(): DownloadingItem {
        val document = Jsoup.connect(url).get()
        val div = document.getElementById("bookimg")
        val img = div.selectFirst("img")
        imgUrl = img.attr("src")
        name = img.attr("alt")

        val elements = document.select("#chapterList li")
        for (i in 0 until elements.size) {
            val url1 = "https://www.x23qb.com" + elements[i].selectFirst("a").attr("href")
            chacterMap.put(i, url1)
        }

        //缓存文件开头，之后需要重复用到
        val end = url.lastIndexOf("/")
        val start = url.indexOf("k") + 2
        tempFileHead = url.substring(start, end) + "_"

        return DownloadingItem(name, imgUrl, 0.0, "0.00%", "0/${chacterMap.size}", "解析信息中",itemPosition)
    }

    fun downloadChacter(context: Context, index: Int): DownloadingItem {
        //目录
        val dir = File(SDUtil.getStoragePath(context, false), "星之小说下载器")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        //缓存文件名处理

        val fileName = tempFileHead + index

        //缓存文件File
        val file = File(dir, fileName + ".txt")

        //章节文件已下载，跳过
        if (!file.exists()) {
            val document = Jsoup.connect(chacterMap[index]).get()
            val mainTextDiv = document.selectFirst("#mlfy_main_text")
            val title = mainTextDiv.selectFirst("h1").text()//章节标题

            val element = document.selectFirst("#TextContent")
            var content = element.text().replace(" ", "\n").replace("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replace("&nbsp;", "");
            content = content.replace("本章未完，点击下一页继续阅读", "").replace("＞＞", "");//章节内容

            //文件写入操作
            file.writeText("$title\n$content\n", charset("GBK"))
        }
        val temp = (index + 1) / (chacterMap.size * 1.0)
        val df = DecimalFormat("0.00")
        val progressText = "${df.format(temp * 100)}%"
        if (temp == 1.0) {
            return DownloadingItem(name, imgUrl, temp * 100, progressText, "${index + 1}/${chacterMap.size}", "合并中",itemPosition)
        }
        return DownloadingItem(name, imgUrl, temp * 100, progressText, "${index + 1}/${chacterMap.size}", "下载中",itemPosition)
    }

    /**
     * 合并文件
     */
    fun mergeFile(context: Context) :Int{
        val dir = File(SDUtil.getStoragePath(context, false), "星之小说下载器").path
        val file = File(dir, "${name.trim()}.txt")
        var bf = StringBuffer("")
        for (i in 0 until chacterMap.size) {
            val tempFile = File(dir, "$tempFileHead$i.txt")
            //获得每个缓存txt文件的文本内容
            if (tempFile.exists()) {
                val bytes = tempFile.readText(charset("gbk"))
                bf.append(bytes)
                //获得内容删除文件
                tempFile.delete()
            }
        }
        //写入
        file.writeText(bf.toString(), charset("gbk"))
        return itemPosition
    }
}