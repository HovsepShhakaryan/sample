package com.vylo.auth.mainauthfragment.data.repository.impl

import android.content.Context
import com.vylo.auth.R
import com.vylo.auth.mainauthfragment.data.endpoint.SignUpSocialApi
import com.vylo.auth.mainauthfragment.data.repository.SignUpSocialRepository
import com.vylo.auth.mainauthfragment.domain.entity.request.Social
import com.vylo.auth.mainauthfragment.domain.entity.request.SocialItem
import com.vylo.auth.mainauthfragment.domain.entity.response.SocialData
import com.vylo.common.api.Resource
import com.vylo.common.api.apiservis.ApiColumn
import com.vylo.common.data.repository.BaseRepo
import com.vylo.common.util.enums.TokenType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.java.KoinJavaComponent

class SignUpSocialRepositoryImpl(
    private val context: Context
) : BaseRepo(), SignUpSocialRepository {

    private val apiBase: ApiColumn by KoinJavaComponent.inject(ApiColumn::class.java)
    private val client = apiBase.getInstance(context).create(SignUpSocialApi::class.java)

    companion object {
        const val facebook_token = "access_token"
        const val google_token = "id_token"
    }

    override suspend fun signInSocial(type: TokenType, social: SocialItem): Resource<SocialData> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        var socialName: String? = null
        when (type) {
            TokenType.FACEBOOK -> {
                socialName = context.resources.getString(R.string.label_facebook)
                builder.addFormDataPart(facebook_token, social.accessToken!!)
            }
            TokenType.GOOGLE -> {
                socialName = context.resources.getString(R.string.label_google)
                builder.addFormDataPart(google_token, social.idToken!!)
            }
        }

        return safeApiCall { client.signInSocial(socialName, builder.build().part(0)) }
    }


}