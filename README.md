# my-list-screenlet
Liferay screenlet for Listview

Here you need to specify Adapter and Implementor classes in xml file as attributes of screenlets. For example:

    <com.darshan.cursorlistviewscreenlet.CursorListScreenlet
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        liferay:adapterClassName="com.darshan.NotesAdapter"
        liferay:autoLoad="false"
        liferay:itemLayoutId="@layout/list_item_messagecard"
        liferay:itemProgressLayoutId="@layout/list_item_load_more"
        liferay:layoutId="@layout/list_default"/>

and

    <com.liferay.mobile.screens.messageboardscreenlet.MyListScreenlet
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        liferay:adapterClassName="com.screens.msgboard.screenlet.adapter.CategoryListAdapter"
        liferay:autoLoad="false"
        liferay:interactorClassName="com.screens.msgboard.screenlet.interactor.CategoryListInteractorImpl"
        liferay:itemLayoutId="@layout/list_item_categorycard"
        liferay:itemProgressLayoutId="@layout/list_item_load_more"
        liferay:layoutId="@layout/grid_default"/>

this adapter class needs to extend respective adapter class and Implementor class needs to extend respective adapter class and implement respective interactor interface. For example:

    public class CategoryListAdapter extends MyListAdapter<Category, CategoryListAdapter.ViewHolder>
and

    public class CategoryListInteractorImpl extends BaseRemoteInteractor<MyListInteractorListener> implements MyListInteractor

and

    public class NotesAdapter extends CursorListAdapter<Note>
