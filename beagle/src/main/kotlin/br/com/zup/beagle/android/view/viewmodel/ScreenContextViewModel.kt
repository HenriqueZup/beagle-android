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

package br.com.zup.beagle.android.view.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.SetContextInternal
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.ContextDataEvaluation
import br.com.zup.beagle.android.context.ContextDataManager
import br.com.zup.beagle.android.context.ImplicitContextManager
import br.com.zup.beagle.android.utils.Observer

@Suppress("TooManyFunctions")
internal class ScreenContextViewModel(
    private val contextDataManager: ContextDataManager = ContextDataManager(),
    private val contextDataEvaluation: ContextDataEvaluation = ContextDataEvaluation(),
    private val implicitContextManager: ImplicitContextManager = ImplicitContextManager()
) : ViewModel() {

    fun setIdToViewWithContext(view: View) {
        contextDataManager.setIdToViewWithContext(view)
    }

    fun addContext(view: View, contextData: ContextData, shouldOverrideExistingContext: Boolean = false) {
        contextDataManager.addContext(view, contextData, shouldOverrideExistingContext)
    }

    fun addContext(view: View, contextDataList: List<ContextData>, shouldOverrideExistingContext: Boolean = false) {
        contextDataManager.addContext(view, contextDataList, shouldOverrideExistingContext)
    }

    fun restoreContext(view: View) {
        contextDataManager.restoreContext(view)
    }

    fun getListContextData(view: View) = contextDataManager.getListContextData(view)

    fun updateContext(originView: View, setContextInternal: SetContextInternal) {
        contextDataManager.updateContext(originView, setContextInternal)
    }

    fun onViewIdChanged(oldId: Int, newId: Int, view: View) {
        contextDataManager.onViewIdChanged(oldId, newId, view)
    }

    fun <T> addBindingToContext(view: View, bind: Bind.Expression<T>, observer: Observer<T?>) {
        contextDataManager.addBinding(view, bind, observer)
    }

    fun linkBindingToContextAndEvaluateThem(view: View) {
        contextDataManager.linkBindingToContextAndEvaluateThem(view)
    }

    fun addImplicitContext(contextData: ContextData, sender: Any, actions: List<Action>) {
        implicitContextManager.addImplicitContext(contextData, sender, actions)
    }

    fun evaluateExpressionForImplicitContext(originView: View, bindCaller: Action, bind: Bind.Expression<*>): Any? {
        val implicitContexts = implicitContextManager.getImplicitContextForBind(bindCaller)
        val contexts = contextDataManager.getContextsFromBind(originView, bind).toMutableList()
        contexts += implicitContexts
        return contextDataEvaluation.evaluateBindExpression(contexts, bind)
    }

    fun evaluateExpressionForGivenContext(originView: View, givenContext: ContextData, bind: Bind.Expression<*>): Any? {
        val contexts = contextDataManager.getContextsFromBind(originView, bind).toMutableList()
        contexts += givenContext
        return contextDataEvaluation.evaluateBindExpression(contexts, bind)
    }

    fun clearContexts() {
        contextDataManager.clearContexts()
    }

    override fun onCleared() {
        super.onCleared()
        clearContexts()
    }

    fun tryLinkContextInBindWithoutContext(originView: View) {
        contextDataManager.tryLinkContextInBindWithoutContext(originView)

    }

    fun getContextData(contextDataId: String): ContextData? = contextDataManager.getContextData(contextDataId)
}
