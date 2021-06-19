package org.teamseven.ols.ui.auth

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.SpinnerAdapter


class HintSpinnerAdapter(
    protected var adapter: SpinnerAdapter,
    hintLayout: Int, hintDropdownLayout: Int, context: Context?
) :
    SpinnerAdapter, ListAdapter {
    protected var context: Context?
    protected var hintLayout: Int
    protected var hintDropdownLayout: Int
    protected var layoutInflater: LayoutInflater

    init {
        this.context = context
        this.hintLayout = hintLayout
        this.hintDropdownLayout = hintDropdownLayout
        layoutInflater = LayoutInflater.from(context)
    }

    /*
    constructor(
        spinnerAdapter: SpinnerAdapter,
        hintLayout: Int, context: Context?
    ) : this(spinnerAdapter, hintLayout, -1, context) {
    }*/

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // This provides the View for the Selected Item in the Spinner, not
        // the dropdown (unless dropdownView is not set).
        return if (position == 0) {
            getHintView(parent)
        } else adapter.getView(position - PIVOT, null, parent)
        // Could re-use
        // the convertView if possible.
    }

    protected fun getHintView(parent: ViewGroup?): View {
        return layoutInflater.inflate(hintLayout, parent, false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Android BUG! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        return if (position == 0) {
            if (hintDropdownLayout == -1) View(context) else getHintDropdownView(parent)
        } else adapter.getDropDownView(position - PIVOT, null, parent)

        // Could re-use the convertView if possible, use setTag...
    }

    protected fun getHintDropdownView(parent: ViewGroup?): View {
        return layoutInflater.inflate(hintDropdownLayout, parent, false)
    }

    override fun getCount(): Int {
        val count = adapter.count
        return if (count == 0) 0 else count + PIVOT
    }

    override fun getItem(position: Int): Any {
        return (if (position == 0) null else adapter.getItem(position - PIVOT))!!
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return if (position >= PIVOT) adapter.getItemId(position - PIVOT) else (position - PIVOT).toLong()
    }

    override fun hasStableIds(): Boolean {
        return adapter.hasStableIds()
    }

    override fun isEmpty(): Boolean {
        return adapter.isEmpty
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        adapter.registerDataSetObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        adapter.unregisterDataSetObserver(observer)
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0 // Don't allow the 'hint' item to be picked.
    }

    companion object {
        protected const val PIVOT = 1
    }


}