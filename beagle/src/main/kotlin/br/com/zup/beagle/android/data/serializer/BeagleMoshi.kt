/*
 * Copyright 2020, 2022 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.android.data.serializer

import androidx.annotation.VisibleForTesting
import br.com.zup.beagle.android.data.serializer.adapter.AnalyticsActionConfigAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.AndroidActionJsonAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.BeagleKotlinJsonAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.BindAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.ComponentJsonAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.ContextDataAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.HttpAdditionalDataAdapter
import br.com.zup.beagle.android.data.serializer.adapter.ImagePathTypeJsonAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.RouteAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.SimpleJsonAdapter
import br.com.zup.beagle.android.data.serializer.adapter.SimpleJsonArrayAdapter
import br.com.zup.beagle.android.data.serializer.adapter.defaults.CharSequenceAdapter
import br.com.zup.beagle.android.data.serializer.adapter.defaults.MoshiArrayListJsonAdapter
import br.com.zup.beagle.android.data.serializer.adapter.defaults.PairAdapterFactory
import br.com.zup.beagle.android.data.serializer.adapter.generic.BeagleGenericAdapterFactory
import br.com.zup.beagle.android.setup.BeagleEnvironment
import com.squareup.moshi.Moshi

internal object BeagleMoshi {

    val moshi: Moshi by lazy {
        createMoshi()
    }

    @VisibleForTesting
    fun createMoshi(): Moshi = Moshi.Builder()
        .add(BindAdapterFactory())
        .add(ImagePathTypeJsonAdapterFactory.make())
        .add(ComponentJsonAdapterFactory.make())
        .add(HttpAdditionalDataAdapter())
        .add(RouteAdapterFactory())
        .add(AnalyticsActionConfigAdapterFactory())
        .add(AndroidActionJsonAdapterFactory.make())
        .add(ContextDataAdapterFactory())
        .add(MoshiArrayListJsonAdapter.FACTORY)
        .add(CharSequenceAdapter())
        .add(PairAdapterFactory)
        .apply {
            BeagleEnvironment.beagleSdk.typeAdapterResolver?.let {
                add(BeagleGenericAdapterFactory(it))
            }
        }
        .add(BeagleKotlinJsonAdapterFactory())
        .add(SimpleJsonAdapter())
        .add(SimpleJsonArrayAdapter())
        .build()
}
