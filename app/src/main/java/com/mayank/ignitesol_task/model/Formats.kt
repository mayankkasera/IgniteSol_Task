package com.mayank.ignitesol_task.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Formats (
    @SerializedName("application/x-mobipocket-ebook")
    @Expose
    val application_x_mobipocket_ebook: String? = null,

    @SerializedName("application/pdf")
    @Expose
    val application_pdf: String? = null,

    @SerializedName("text/plain; charset=us-ascii")
    @Expose
    val text_plain_charset_us_ascii: String? = null,

    @SerializedName("text/plain; charset=utf-8")
    @Expose
    val text_plain_charset_utf_8: String? = null,

    @SerializedName("application/rdf+xml")
    @Expose
    val application_rdf_xml: String? = null,

    @SerializedName("application/zip")
    @Expose
    val application_zip: String? = null,

    @SerializedName("application/epub+zip")
    @Expose
    val application_epub_zip: String? = null,

    @SerializedName("text/html; charset=utf-8")
    @Expose
    val text_html_charset_utf_8: String? = null,


    @SerializedName("image/jpeg")
    @Expose
    val image_jpeg: String? = null

)