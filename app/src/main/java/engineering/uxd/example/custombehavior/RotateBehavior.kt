/*
 * Copyright 2018 Nazmul Idris. All rights reserved.
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

package engineering.uxd.example.custombehavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class RotateBehavior(context: Context, attrs: AttributeSet) :
        CoordinatorLayout.Behavior<FloatingActionButton>(context, attrs),
        AnkoLogger {

    override fun layoutDependsOn(parent: CoordinatorLayout,
                                 child: FloatingActionButton,
                                 dependency: View): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout,
                                        child: FloatingActionButton,
                                        dependency: View): Boolean {
        val translationY = getFabTranslationYForSnackbar(parent, child)
        val percentComplete = -translationY / dependency.height
        child.rotation = -90 * percentComplete
        child.translationY = translationY
        return false
    }

    private fun getFabTranslationYForSnackbar(parent: CoordinatorLayout,
                                              fab: FloatingActionButton): Float {
        var minOffset = 0f
        val dependencies = parent.getDependencies(fab)

        dependencies.forEach {

            if (it is Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, it)) {
                minOffset = Math.min(minOffset, it.getTranslationY() - it.getHeight())
            }

        }

        info {
            "rotate.minOffset=$minOffset"
        }

        return minOffset
    }

}
