/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.livemap.entities.placement

import jetbrains.livemap.LiveMapContext
import jetbrains.livemap.LiveMapSystem
import jetbrains.livemap.camera.ZoomChangedComponent
import jetbrains.livemap.core.ecs.EcsComponentManager
import jetbrains.livemap.core.rendering.layers.ParentLayerComponent
import jetbrains.livemap.projections.ClientPoint
import jetbrains.livemap.projections.WorldPoint
import jetbrains.livemap.projections.WorldProjection

class WorldDimension2ScreenUpdateSystem(componentManager: EcsComponentManager) : LiveMapSystem(componentManager) {

    override fun updateImpl(context: LiveMapContext, dt: Double) {
        if (camera().isIntegerZoom) {
            for (worldEntity in getEntities(COMPONENT_TYPES)) {
                worldEntity.get<WorldDimensionComponent>()
                    .dimension
                    .let { world2Screen(it, camera().zoom.toInt()) }
                    .let { worldEntity.provide(::ScreenDimensionComponent).dimension = it }
                
                ParentLayerComponent.tagDirtyParentLayer(worldEntity)
            }
        }
    }

    companion object {

        private val COMPONENT_TYPES = listOf(
            ZoomChangedComponent::class,
            WorldDimensionComponent::class,
            ParentLayerComponent::class
        )

        fun world2Screen(p: WorldPoint, zoom: Int): ClientPoint {
            return WorldProjection(zoom).project(p)
        }
    }
}