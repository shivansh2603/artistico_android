package com.example.artistico_android.core.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Auto-clears the binding when the Fragment's view is destroyed to avoid memory leaks.
 */
class FragmentViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    private val bind: (android.view.View) -> T
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewOwner: LifecycleOwner? ->
            viewOwner?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    binding = null
                }
            })
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }
        val view = thisRef.requireView()
        return bind(view).also { binding = it }
    }
}

inline fun <T : ViewBinding> Fragment.viewBinding(
    crossinline bind: (android.view.View) -> T
) = FragmentViewBindingDelegate(this) { bind(it) }
