package com.example.michailgromtsev.startandroidvk.common.manager;



import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;

import com.example.michailgromtsev.startandroidvk.ui.activity.BaseActivity;
import com.example.michailgromtsev.startandroidvk.ui.fragment.BaseFragment;

import java.util.Stack;

// this is castcom fragment manager

public class MyFragmentManager {
    //minimum size of mFragmentStack - any time we mast have 1 ore more elements in that stack
    private static  final int EMRTY_FRAGMENT_STACK_SIZE = 1;

    private Stack<BaseFragment> mFragmentStack = new Stack<>();
    private BaseFragment mCurrentFragment;


    //устнавливаем коренвой фрагмент - в нем устанвдиваются загоолвки окна и видимость floating action button
    public void setFragment (BaseActivity activity, BaseFragment fragment, @IdRes int conteinerId) {
        if (activity != null && !activity.isFinishing() && !isAlreadyContains(fragment)) {
            FragmentTransaction transaction = createAddTransaction(activity,fragment,false);
            transaction.replace(conteinerId, fragment);
            commitAddTransaction(activity, fragment, transaction,false );
        }
    }

    // а этот  метод добавляет фрагмент поверх корневого  беря их согласго пункту из меню навигации
    public void addFragment (BaseActivity activity, BaseFragment fragment,@IdRes int conteinerId) {
        if (activity != null && !activity.isFinishing() && !isAlreadyContains(fragment)) {
            FragmentTransaction transaction = createAddTransaction(activity,fragment,true);
            transaction.replace(conteinerId,fragment);
            commitAddTransaction(activity,fragment,transaction,true);
        }
    }

    //удаление текущего фрагмента
    public boolean removeCurrentFragment (BaseActivity activity) {
        return removeFragment(activity,mCurrentFragment);
    }


    // удаление из фрагмента из стека
    public boolean removeFragment (BaseActivity activity, BaseFragment fragment) {
        boolean canRemove = fragment != null && mFragmentStack.size() > EMRTY_FRAGMENT_STACK_SIZE;
        if (canRemove) {
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

            mFragmentStack.pop();
            mCurrentFragment = mFragmentStack.lastElement();

            transaction.remove(fragment);
            commitTransaction(activity,transaction);
        }

        return canRemove;
    }


    // in depends of addToBackStack param create fragment transaction with adding fragment to top of stack or make new stack
    private FragmentTransaction createAddTransaction(BaseActivity activity, BaseFragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();

        if(addToBackStack) {
                fragmentTransaction.addToBackStack(fragment.getTag());
        }

        return  fragmentTransaction;
    }


    // commit trnsaction в зависмости от addToBackStack очищает Бэкстэк и добаляет в топ.
   private void commitAddTransaction(BaseActivity activity, BaseFragment fragment,
                                     FragmentTransaction transaction, boolean addToBackStack) {
    if (transaction != null) {
        mCurrentFragment = fragment;

        if (!addToBackStack) {
            mFragmentStack = new Stack<>();
        }

        mFragmentStack.add(fragment);

        commitTransaction(activity,transaction);
    }
   }
    //commit any transactions. don't care add or delete fragment to/from stack
    private void commitTransaction (BaseActivity activity, FragmentTransaction transaction) {
        transaction.commit();

        activity.fragmentOnScreen(mCurrentFragment);
    }

    // проврка - существует ли уже в стеке фрагмент
    public boolean isAlreadyContains (BaseFragment baseFragment) {
        if (baseFragment == null) {
            return false;
        }

        return mCurrentFragment != null && mCurrentFragment.getClass().getName().equals(
                baseFragment.getClass().getName() );
    }


}
