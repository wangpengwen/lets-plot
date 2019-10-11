package jetbrains.datalore.plot.builder.scale.mapper

import jetbrains.datalore.plot.base.render.linetype.LineType
import jetbrains.datalore.plot.base.render.linetype.NamedLineType

object LineTypeMapper {
    val NA_VALUE: LineType = NamedLineType.SOLID

    fun allLineTypes(): List<LineType> {
        return listOf(
                NamedLineType.SOLID,
                NamedLineType.DASHED,
                NamedLineType.DOTTED,
                NamedLineType.DOTDASH,
                NamedLineType.LONGDASH,
                NamedLineType.TWODASH
        )
    }
}
