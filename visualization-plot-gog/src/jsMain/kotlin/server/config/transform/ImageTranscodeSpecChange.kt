package jetbrains.datalore.visualization.plot.gog.server.config.transform

import jetbrains.datalore.visualization.plot.gog.config.Option
import jetbrains.datalore.visualization.plot.gog.config.transform.SpecChange
import jetbrains.datalore.visualization.plot.gog.config.transform.SpecChangeContext

internal actual class ImageTranscodeSpecChange : SpecChange {
    override fun isApplicable(spec: Map<String, Any>): Boolean {
        return Option.GeomName.IMAGE == spec[Option.Layer.GEOM] && spec.containsKey(Option.Geom.Image.SPEC)
    }

    override fun apply(spec: MutableMap<String, Any>, ctx: SpecChangeContext) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

