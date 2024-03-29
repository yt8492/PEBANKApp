package com.yt8492.pe_bankapp.view.fragment


import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso

import com.yt8492.pe_bankapp.R
import com.yt8492.pe_bankapp.databinding.FragmentMapBinding
import com.yt8492.pe_bankapp.model.datamodel.Cell
import com.yt8492.pe_bankapp.model.state.Status
import com.yt8492.pe_bankapp.util.toast
import com.yt8492.pe_bankapp.viewmodel.MapViewModel
import com.yt8492.pe_bankapp.viewmodel.factory.MapViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.math.absoluteValue

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    @Inject
    internal lateinit var viewModelFactory: MapViewModelFactory

    private val mapColumns by lazy {
        arguments?.getInt(KEY_MAP_COLUMNS) ?: 0
    }

    private val mapRows by lazy {
        arguments?.getInt(KEY_MAP_ROWS) ?: 0
    }

    private val viewModel: MapViewModel by viewModels { viewModelFactory }

    private val cells by lazy {
        List(mapColumns) { x ->
            List(mapRows) { y ->
                Cell(x, y, Color.valueOf(1f, 0f, 0f, 0f))
            }
        }
    }
    private var previousSelectedCell: Cell? = null
    private var previousVisitedCell: Cell? = null
    private val selectedCells = mutableListOf<Cell>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        with(binding) {
            lifecycleOwner = this@MapFragment
            setupMapTable(mapTable, routeRegisterButton, routeDeleteButton)
        }
        return binding.root
    }

    private fun setupMapTable(mapTable: TableLayout, registerButton: Button, deleteButton: Button) {
        val rowParams = TableLayout.LayoutParams(
            0,
            0,
            1f
        )
        val rows = List(mapRows) { y ->
            TableRow(context).apply {
                layoutParams = rowParams
                gravity = Gravity.CENTER_VERTICAL
                dividerDrawable = ResourcesCompat.getDrawable(resources, R.drawable.table_dividier, null)
                showDividers = TableLayout.SHOW_DIVIDER_MIDDLE
                List(mapColumns) { x ->
                    View(context).apply {
                        background = cells[x][y].color.toDrawable()
                    }
                }.forEach(this::addView)
            }
        }
        rows.forEach(mapTable::addView)
        mapTable.children.mapNotNull {
            (it as? TableRow)?.children
        }.flatten().forEach {
            val y = binding.mapTable.indexOfChild(it.parent as View)
            val x = (binding.mapTable[y] as ViewGroup).indexOfChild(it)
            it.setOnClickListener {
                val cell = cells[x][y]
                if (selectedCells.size >= 12) {
                    toast("これ以上セルを選択できません。")
                } else if (previousSelectedCell?.isNextTo(cell) == false) {
                    toast("直前に選択したセルに隣接していません。")
                } else {
                    previousSelectedCell = cell
                    selectedCells.add(cell)
                    cell.color = Color.valueOf(1f, 0f, 0f, minOf(1f, cell.color.alpha() + 0.1f))
                    it.background = cell.color.toDrawable()
                }
            }
        }

        registerButton.setOnClickListener {
            viewModel.registerRoute(selectedCells.map { it.toModel() })
        }

        deleteButton.setOnClickListener {
            previousSelectedCell = null
            selectedCells.clear()
            mapTable.children.mapNotNull {
                (it as? TableRow)?.children
            }.flatten().forEach {
                it.background = ColorDrawable(Color.valueOf(0xFF000000).toArgb())
            }
            cells.flatten().forEach {
                it.color = Color.valueOf(1f, 0f, 0f, 0f)
            }
            resetGame()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer {
            fun setCell(cell: com.yt8492.pe_bankapp.model.datamodel.Cell) {
                previousVisitedCell?.let { previousCell ->
                    (binding.mapTable[previousCell.y] as ViewGroup)[previousCell.x]
                        .background = cells[previousCell.x][previousCell.y].color.toDrawable()
                }
                val currentCell = cells[cell.x][cell.y]
                (binding.mapTable[currentCell.y] as ViewGroup)[currentCell.x]
                    .background = Color.valueOf(0f, 0f, 1f, 0.2f).toDrawable()
                previousVisitedCell = currentCell
            }
            if (it is Status.GameFinished) {
                toast(it.message)
                resetGame()
            } else {
                when(it) {
                    is Status.Flight -> {
                        setCell(it.cell)
                        toast("何もありませんでした。")
                    }
                    is Status.Crash -> {
                        setCell(it.cell)
                        toast("墜落しました。")
                    }
                    is Status.FetchedKey -> {
                        setCell(it.cell)
                        toast("鍵${it.key.name}を入手しました。")
                    }
                    is Status.KeyOverflowed -> {
                        setCell(it.cell)
                        val keys = it.ownKeys
                        AlertDialog.Builder(requireContext())
                            .setTitle("捨てる鍵を選んでください")
                            .setItems(keys.map { "鍵${it.name}" }.toTypedArray()) { _, witch ->
                                viewModel.deleteKey(keys[witch])
                            }
                            .create()
                            .show()
                    }
                    is Status.FetchingTreasureSuccess -> {
                        setCell(it.cell)
                        val treasureImageView = ImageView(requireContext()).apply {
                            Picasso.get()
                                .load(it.treasure.imageUrl)
                                .into(this)
                        }
                        AlertDialog.Builder(requireContext())
                            .setTitle("お宝入手！")
                            .setMessage("お宝: ${it.treasure.name}")
                            .setView(treasureImageView)
                            .setPositiveButton("OK") { _, _ ->
                                viewModel.userWaiting = false
                            }
                            .create()
                            .show()
                    }
                    is Status.FetchingTreasureFailure -> {
                        setCell(it.cell)
                        toast("宝箱がありましたが合う鍵がありませんでした。")
                    }
                }
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message != null) {
                toast(message)
            }
        })
    }

    private fun resetGame() {
        previousSelectedCell = null
        selectedCells.clear()
        binding.mapTable.children.mapNotNull {
            (it as? TableRow)?.children
        }.flatten().forEach {
            it.background = ColorDrawable(Color.valueOf(0xFF000000).toArgb())
        }
        cells.flatten().forEach {
            it.color = Color.valueOf(1f, 0f, 0f, 0f)
        }
        viewModel.finishGame()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        private const val KEY_MAP_COLUMNS = "MAP_COLUMNS"
        private const val KEY_MAP_ROWS = "MAP_ROWS"

        @JvmStatic
        fun newInstance(mapColumns: Int, mapRows: Int) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_MAP_COLUMNS, mapColumns)
                    putInt(KEY_MAP_ROWS, mapRows)
                }
            }
    }

    data class Cell(val x: Int, val y: Int, var color: Color) {
        fun isNextTo(other: Cell): Boolean {
            return (x - other.x == 0 && (y - other.y).absoluteValue == 1) ||
                    ((x - other.x).absoluteValue == 1 && y - other.y == 0)
        }

        fun toModel(): com.yt8492.pe_bankapp.model.datamodel.Cell {
            return Cell(x, y)
        }
    }
}
