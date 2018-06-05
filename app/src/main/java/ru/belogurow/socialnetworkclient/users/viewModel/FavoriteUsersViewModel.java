package ru.belogurow.socialnetworkclient.users.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.belogurow.socialnetworkclient.App;
import ru.belogurow.socialnetworkclient.common.web.Resource;
import ru.belogurow.socialnetworkclient.users.dto.FavoriteUsersDto;
import ru.belogurow.socialnetworkclient.users.model.FavoriteUsers;
import ru.belogurow.socialnetworkclient.users.repository.RemoteFavoriteUsersRepository;

public class FavoriteUsersViewModel extends ViewModel {

    private static final String TAG = FavoriteUsersViewModel.class.getSimpleName();

    @Inject
    protected RemoteFavoriteUsersRepository mRemoteFavoriteUsersRepository;

    private CompositeDisposable mCompositeDisposable;

    public FavoriteUsersViewModel() {
        App.getComponent().inject(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    public LiveData<Resource<FavoriteUsersDto>> addToFavoriteUsers(FavoriteUsers favoriteUsers) {
        Log.d(TAG, "addToFavoriteUsers: " + favoriteUsers);

        final MutableLiveData<Resource<FavoriteUsersDto>> favoriteUsersResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteFavoriteUsersRepository.addToFavoriteUsers(favoriteUsers)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                favoriteUsersDto -> {
                                    Log.d(TAG, "addToFavoriteUsers-result: " + favoriteUsersDto);
                                    favoriteUsersResult.postValue(Resource.success(favoriteUsersDto));
                                },
                                error -> {
                                    Log.d(TAG, "addToFavoriteUsers-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        favoriteUsersResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        favoriteUsersResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return favoriteUsersResult;
    }

    public LiveData<Resource<List<FavoriteUsersDto>>> getAllFavoriteUsersByUserId(UUID fromUserId) {
        Log.d(TAG, "getAllFavoriteUsersByUserId: " + fromUserId);

        final MutableLiveData<Resource<List<FavoriteUsersDto>>> favoriteUsersResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteFavoriteUsersRepository.getAllFavoriteUsersByUserId(fromUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                favoriteUsersDto -> {
                                    Log.d(TAG, "getAllFavoriteUsersByUserId-result: " + fromUserId);
                                    favoriteUsersResult.postValue(Resource.success(favoriteUsersDto));
                                },
                                error -> {
                                    Log.d(TAG, "getAllFavoriteUsersByUserId-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        favoriteUsersResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        favoriteUsersResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return favoriteUsersResult;
    }

    public LiveData<Resource<Boolean>> isFromUserIdHasFavToUserId(UUID fromUserId, UUID toUserId) {
        Log.d(TAG, "isFromUserIdHasFavToUserId: " + fromUserId + " " + toUserId);

        final MutableLiveData<Resource<Boolean>> isFavoriteResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteFavoriteUsersRepository.isFromUserIdHasFavToUserId(fromUserId, toUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isFavorite -> {
                                    Log.d(TAG, "isFromUserIdHasFavToUserId-result: " + isFavorite);
                                    isFavoriteResult.postValue(Resource.success(isFavorite));
                                },
                                error -> {
                                    Log.d(TAG, "isFromUserIdHasFavToUserId-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        isFavoriteResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        isFavoriteResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return isFavoriteResult;
    }

    public LiveData<Resource<Boolean>> deleteFromUserIdFavForToUserId(UUID fromUserId, UUID toUserId) {
        Log.d(TAG, "deleteFromUserIdFavForToUserId: " + fromUserId + " " + toUserId);

        final MutableLiveData<Resource<Boolean>> isDeleteResult = new MutableLiveData<>();

        mCompositeDisposable.add(
                mRemoteFavoriteUsersRepository.deleteFromUserIdFavForToUserId(fromUserId, toUserId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                isDeleted -> {
                                    Log.d(TAG, "deleteFromUserIdFavForToUserId-result: " + isDeleted);
                                    isDeleteResult.postValue(Resource.success(isDeleted));
                                },
                                error -> {
                                    Log.d(TAG, "deleteFromUserIdFavForToUserId-error: " + Arrays.toString(error.getStackTrace()));

                                    if (error instanceof HttpException)
                                        isDeleteResult.postValue(Resource.error(((HttpException) error).response().errorBody()));
                                    else {
                                        isDeleteResult.postValue(Resource.error(error.getMessage()));
                                    }
                                })

        );

        return isDeleteResult;
    }


    @Override
    protected void onCleared() {
        super.onCleared();

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
