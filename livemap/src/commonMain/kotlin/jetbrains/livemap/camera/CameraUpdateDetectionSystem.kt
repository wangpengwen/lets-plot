/*
 * Copyright (c) 2019. JetBrains s.r.o.
 * Use of this source code is governed by the MIT license that can be found in the LICENSE file.
 */

package jetbrains.livemap.camera

import jetbrains.livemap.LiveMapContext
import jetbrains.livemap.core.ecs.AbstractSystem
import jetbrains.livemap.core.ecs.EcsComponentManager
import jetbrains.livemap.entities.Entities
import jetbrains.livemap.projections.Coordinates
import jetbrains.livemap.projections.WorldPoint

class CameraUpdateDetectionSystem(componentManager: EcsComponentManager) :
    AbstractSystem<LiveMapContext>(componentManager) {
    private var myPreviousCameraZoom: Double = 0.0
    private var myPreviousCameraCenter: WorldPoint = Coordinates.ZERO_WORLD_POINT

    override fun initImpl(context: LiveMapContext) {
        val viewport = context.mapRenderContext.viewport
        createEntity("camera")
            .addComponent(CameraUpdateComponent())
            .addComponent(
                CameraComponent(
                    viewport.zoom.toDouble(),
                    viewport.position
                )
            )
    }

    override fun updateImpl(context: LiveMapContext, dt: Double) {
        val cameraEntity = Entities.camera(componentManager)
        val camera = cameraEntity.get<CameraComponent>()
        val cameraUpdate = cameraEntity.get<CameraUpdateComponent>()

        removeChangedComponents()

        cameraUpdate.isZoomChanged = myPreviousCameraZoom != camera.zoom
        cameraUpdate.isMoved = myPreviousCameraCenter != camera.position

        myPreviousCameraZoom = camera.zoom
        myPreviousCameraCenter = camera.position

        if (cameraUpdate.isZoomChanged || cameraUpdate.isMoved) {
            updateAll(cameraUpdate.isZoomChanged, cameraUpdate.isMoved)
        }
    }

    private fun removeChangedComponents() {
        for (entity in getEntities(ZoomChangedComponent::class).toList()) {
            entity.removeComponent(ZoomChangedComponent::class)
        }

        for (entity in getEntities(CenterChangedComponent::class).toList()) {
            entity.removeComponent(CenterChangedComponent::class)
        }
    }

    private fun updateAll(zoomChanged: Boolean, centerChanged: Boolean) {
        for (entity in getEntities(CameraListenerComponent::class)) {
            if (zoomChanged) {
                entity.tag(::ZoomChangedComponent)
                entity.tag(::CenterChangedComponent)
            }

            if (centerChanged) {
                entity.tag(::CenterChangedComponent)
            }
        }
    }
}