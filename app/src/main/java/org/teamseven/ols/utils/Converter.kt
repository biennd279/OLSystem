package org.teamseven.ols.utils

import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.teamseven.ols.entities.Data
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class DataConverter<T>(private val converter: Converter<ResponseBody, Data<T>>): Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        return converter.convert(value)?.data
    }
}

class DataConverterFactory: Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val dataType = TypeToken.getParameterized(Data::class.java, type).type
        val converter = retrofit.nextResponseBodyConverter<Data<Any>>(this, dataType, annotations)
        return DataConverter(converter)
    }

    //TODO: make singleton Factory
}