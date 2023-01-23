package com.vylo.main.responsemain.upload.domain.usecase.impl

import com.vylo.main.responsemain.upload.domain.entity.request.ColumnNews
import com.vylo.main.responsemain.upload.domain.usecase.ColumnNewsUseCase

class ColumnNewsUseCaseImpl : ColumnNewsUseCase {

    private var category: String? = null
    private var title: String? = null
    private var description: String? = null
    private var link: String? = null
    private var author: String? = null
    private var responseTo: String? = null
    private var status: Long? = null

    override fun setCategory(category: String) {
        this.category = category
    }

    override fun setTitle(title: String) {
        this.title = title
    }

    override fun setDescription(description: String) {
        this.description = description
    }

    override fun setLink(link: String) {
        this.link = link
    }

    override fun setAuthor(author: String) {
        this.author = author
    }

    override fun setResponseTo(responseTo: String) {
        this.responseTo = responseTo
    }

    override fun clearResponseTo() {
        this.responseTo = null
    }

    override fun setStatus(status: Long) {
        this.status = status
    }

    override fun createColumnNewsModel() = ColumnNews(
        category,
        title,
        description,
        link,
        author,
        responseTo,
        status
    )
}