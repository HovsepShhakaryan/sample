package com.vylo.main.responsemain.upload.domain.usecase

import com.vylo.main.responsemain.upload.domain.entity.request.ColumnNews

interface ColumnNewsUseCase {
    fun setCategory(category: String)
    fun setTitle(title: String)
    fun setDescription(description: String)
    fun setLink(link: String)
    fun setAuthor(author: String)
    fun setResponseTo(responseTo: String)
    fun clearResponseTo()
    fun setStatus(status: Long)
    fun createColumnNewsModel(): ColumnNews
}