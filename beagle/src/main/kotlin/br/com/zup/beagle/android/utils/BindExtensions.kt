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

package br.com.zup.beagle.android.utils

import android.view.View
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView

typealias Observer<T> = (value: T) -> Unit

/**
 * Observe a specific Bind to changes. If the Bind is type of Value, then the actual value will be returned.
 * But if the value is an Expression, then the evaluation will be make.
 * @property rootView from buildView
 * @property view that will receive the binding
 * @property observes is function that will be called when a expression is evaluated
 */
fun <T> Bind<T>.observe(
    rootView: RootView,
    view: View,
    observes: Observer<T?>? = null,
) {
    val value = evaluateBind(rootView, view, this, null, observes)
    if (this is Bind.Value) {
        observes?.invoke(value)
    }
}

/**
 * Evaluate the expression to a value
 * @property rootView from buildView
 * @property origin received on execute method
 * @property caller action called function
 */
fun <T> Bind<T>.evaluate(
    rootView: RootView,
    origin: View,
    caller: Action,
): T? {
    return evaluateBind(rootView, origin, this, caller, null)
}

private fun <T> evaluateBind(
    rootView: RootView,
    view: View,
    bind: Bind<T>,
    caller: Action?,
    observes: Observer<T?>?,
): T? {

    @Suppress("UNCHECKED_CAST")
    return try {
        when (bind) {
            is Bind.Expression -> evaluateExpression(rootView, view, bind, observes, caller)
            else -> bind.value as? T?
        }
    } catch (ex: Exception) {
        BeagleMessageLogs.errorWhileTryingToEvaluateBinding(ex)
        null
    }
}

private fun <T> evaluateExpression(
    rootView: RootView,
    view: View,
    bind: Bind.Expression<T>,
    observes: Observer<T?>? = null,
    caller: Action? = null,
): T? {
    val viewModel = rootView.generateViewModelInstance<ScreenContextViewModel>()
    return if (caller != null) {
        @Suppress("UNCHECKED_CAST")
        viewModel.evaluateExpressionForImplicitContext(view, caller, bind) as? T?
    } else {
        observes?.let {
            viewModel.addBindingToContext(view, bind, it)
        }
        null
    }
}