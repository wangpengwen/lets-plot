package jetbrains.datalore.visualization.plotDemo.model.plotConfig

import jetbrains.datalore.visualization.plot.parsePlotSpec
import jetbrains.datalore.visualization.plotDemo.model.Iris
import jetbrains.datalore.visualization.plotDemo.model.PlotConfigDemoBase

class Density2d : PlotConfigDemoBase() {
    fun plotSpecList(): List<Map<String, Any>> {
        return listOf(
            sepanLength()
        )
    }

    private fun sepanLength(): Map<String, Any> {
        val spec = "{" +
                "   'mapping': {" +
                "             'x': 'sepal length (cm)'," +
                "             'y': 'sepal width (cm)'," +
                "             'color': 'target'" +
                "           }," +

                "   'layers': [" +
                "               {" +
                "                  'geom': 'density2d'" +
                "               }" +
                "           ]" +
                "}"

        val plotSpec = HashMap(parsePlotSpec(spec))
        plotSpec["data"] = Iris.df
        return plotSpec

    }
}