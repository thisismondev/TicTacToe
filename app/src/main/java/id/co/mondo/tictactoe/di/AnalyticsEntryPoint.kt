package id.co.mondo.tictactoe.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mondo.tictactoe.util.AnalyticsHelper

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AnalyticsEntryPoint {
    fun analyticsHelper(): AnalyticsHelper
}
