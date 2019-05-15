package jetbrains.datalore.visualization.gogProjectionalDemo.model.cell

import jetbrains.datalore.base.geometry.DoubleVector
import jetbrains.datalore.base.observable.property.ValueProperty
import jetbrains.datalore.base.values.Color
import jetbrains.datalore.visualization.plot.gog.core.data.DataFrameUtil
import jetbrains.datalore.visualization.plot.gog.core.data.stat.Stats
import jetbrains.datalore.visualization.plot.gog.core.render.Aes
import jetbrains.datalore.visualization.plot.gog.core.scale.Scale2
import jetbrains.datalore.visualization.plot.gog.core.scale.Scales
import jetbrains.datalore.visualization.plot.gog.plot.PlotContainer
import jetbrains.datalore.visualization.plot.gog.plot.VarBinding
import jetbrains.datalore.visualization.plot.gog.plot.assemble.GeomLayerBuilder
import jetbrains.datalore.visualization.plot.gog.plot.assemble.PlotAssembler
import jetbrains.datalore.visualization.plot.gog.plot.assemble.PosProvider
import jetbrains.datalore.visualization.plot.gog.plot.assemble.geom.GeomProvider
import jetbrains.datalore.visualization.plot.gog.plot.coord.CoordProviders
import jetbrains.datalore.visualization.plot.gog.plot.theme.DefaultTheme

class BarPlotXAxisDemo private constructor(
        private val sclData: SinCosLineData,
        private val xScale: Scale2<*>) {

    val plotSize = ValueProperty(DoubleVector(600.0, 400.0))

    fun createPlot(): PlotContainer {
        val varX = sclData.varX
        val varY = sclData.varY
        val varCat = sclData.varCat
        val data = sclData.dataFrame

        val categories = ArrayList(DataFrameUtil.distinctValues(data, varCat))
        val colors = listOf(Color.RED, Color.BLUE, Color.CYAN)
        val fillScale = Scales.pureDiscrete("C", categories, colors, Color.GRAY)
        val layer = GeomLayerBuilder.demoAndTest()
                .stat(Stats.IDENTITY)
                .geom(GeomProvider.bar())
                .pos(PosProvider.dodge())
                .groupingVar(varCat)
                .addBinding(VarBinding(varX, Aes.X, xScale))
                .addBinding(VarBinding(varY, Aes.Y, Scales.continuousDomain("sin, cos, line", Aes.Y)))
                .addBinding(VarBinding(varCat, Aes.FILL, fillScale))
                .addConstantAes(Aes.WIDTH, 0.9)
                .build(data)

        //Theme t = new DefaultTheme() {
        //  @Override
        //  public AxisTheme axisX() {
        //    return new DefaultAxisTheme() {
        //      @Override
        //      public boolean showTickLabels() {
        //        return false;
        //      }
        //    };
        //  }
        //};
        val assembler = PlotAssembler.singleTile(listOf(layer), CoordProviders.cartesian(), DefaultTheme())
        assembler.disableInteractions()
        return PlotContainer(assembler.createPlot(), plotSize)
    }

    companion object {

        fun continuousX(): BarPlotXAxisDemo {
            return BarPlotXAxisDemo(
                    SinCosLineData({ v -> v }, 6),
                    Scales.continuousDomain(" ", Aes.X))
        }

        fun discreteX(): BarPlotXAxisDemo {
            val sclData = SinCosLineData({ v -> "Group label " + (v + 1) }, 6)
            return BarPlotXAxisDemo(
                    sclData,
                    Scales.discreteDomain<String>("", sclData.distinctXValues()))
        }
    }
}