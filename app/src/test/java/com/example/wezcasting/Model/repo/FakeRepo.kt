package com.example.wezcasting.Model.repo

import com.example.wezcasting.Networking.IWeatherRemoteDataSource
import com.example.wezcasting.db.IWeatherLocalDataSource

class FakeRepo(
    private val remoteDataSource: IWeatherRemoteDataSource,
    private val localDataSource: IWeatherLocalDataSource
) : RepoInterface {