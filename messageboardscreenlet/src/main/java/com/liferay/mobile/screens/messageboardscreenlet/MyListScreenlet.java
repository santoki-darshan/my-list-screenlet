package com.liferay.mobile.screens.messageboardscreenlet;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.liferay.mobile.screens.base.list.BaseListScreenlet;
import com.liferay.mobile.screens.messageboardscreenlet.interactor.MyListInteractor;
import com.liferay.mobile.screens.messageboardscreenlet.interactor.MyListInteractorListener;
import com.liferay.mobile.screens.messageboardscreenlet.model.MyModel;
import com.liferay.mobile.screens.messageboardscreenlet.view.MyListViewModel;

import java.util.Locale;

/**
 * Created by darshan on 6/8/15.
 */
public class MyListScreenlet extends BaseListScreenlet<MyModel, MyListInteractor> implements MyListInteractorListener<MyModel> {

    private String _interactorClassName, adapterClassName;
    private int layoutId, progressLayoutId;

    public MyListScreenlet(Context context) {
        super(context);
    }

    public MyListScreenlet(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public MyListScreenlet(Context context, AttributeSet attributes, int defaultStyle) {
        super(context, attributes, defaultStyle);
    }

    @Override
    protected void loadRows(MyListInteractor interactor, int startRow, int endRow, Locale locale) throws Exception {
        interactor.loadRows(startRow, endRow);
    }

    @Override
    protected View createScreenletView(Context context, AttributeSet attributes) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributes, R.styleable.MyListScreenlet, 0, 0);

        _interactorClassName = typedArray.getNonResourceString(R.styleable.MyListScreenlet_interactorClassName);

        layoutId = typedArray.getResourceId(R.styleable.MyListScreenlet_itemLayoutId, getDefaultItemLayoutId());

        progressLayoutId = typedArray.getResourceId(R.styleable.MyListScreenlet_itemProgressLayoutId, getDefaultItemProgressLayoutId());

        adapterClassName = typedArray.getNonResourceString(R.styleable.MyListScreenlet_adapterClassName);

        typedArray.recycle();

        return super.createScreenletView(context, attributes);
    }

    @Override
    protected void onFinishInflate() {
        ((MyListViewModel) getViewModel()).setAdapter(adapterClassName, layoutId, progressLayoutId);
        super.onFinishInflate();
    }

    protected int getDefaultItemLayoutId() {
        return R.layout.list_item_messagecard;
    }

    protected int getDefaultItemProgressLayoutId() {
        return R.layout.list_item_load_more;
    }

    @Override
    protected MyListInteractor createInteractor(String actionName) {
        try {
            return (MyListInteractor) Class.forName(_interactorClassName).getConstructor(int.class).newInstance(getScreenletId());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void refresh() {
        loadPage(0);
    }

    @Override
    public void setTotalCount(Integer result) {
        ((MyListViewModel) getViewModel()).setListCount(result);
    }
}
