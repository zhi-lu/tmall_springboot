package com.luzhi.tmall.util;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author apple
 * @version jdk8
 * // TODO : 2021/3/21
 * 页面导航处理.进行相关的编排.....
 */

@SuppressWarnings("unused")
public class Page4Navigator<T> {

    /**
     * @see #pageFromJpa
     * 此属性对jpa传递出返回对象, Page4Navigator对其封装进行扩展。
     */
    Page<T> pageFromJpa;

    /**
     * @see #navigatePages
     * 捕获当前页面对导航总数.如果大于size值往后展示[6,7,8,9,10]
     */
    int navigatePages;

    /**
     * @see #totalPages
     * 总页数
     */
    int totalPages;
    /**
     * @see #number
     * 第几页面
     */
    int number;
    /**
     * @see #totalPages
     * 总共有多少条数据...
     */
    long totalElements;
    /**
     * @see #size
     * 一页最多有多少条数据..
     */
    int size;
    /**
     * @see #numberOfElements
     * 一个页面有多少条数据....(不满足规定的size 5)
     */
    int numberOfElements;
    /**
     * @see #content
     * 数据集合
     */
    List<T> content;
    /**
     * @see #isHasContent
     * 是否有数据
     */
    boolean isHasContent;
    /**
     * @see #first
     * 是否为第一页面
     */
    boolean first;
    /**
     * @see #last
     * 是否为最后页面.
     */
    boolean last;
    /**
     * @see #isHasNext
     * 是否存在下一页面.
     */
    boolean isHasNext;
    /**
     * @see #isHasPrevious
     * 是否存在上一页面
     */
    boolean isHasPrevious;
    /**
     * @see #navigatePageNums
     * 分页的时候 ,如果总页数比较多，那么显示出来的分页超链一个有几个。 比如如果分页出来的超链是这样的：
     */
    int[] navigatePageNums;

    public Page4Navigator() {
        //这个空的分页是为了 Redis 从 json格式转换为 Page4Navigator 对象而专门提供的
    }

    public Page4Navigator(Page<T> pageFromJpa, int navigatePages) {
        this.pageFromJpa = pageFromJpa;
        this.navigatePages = navigatePages;

        totalPages = pageFromJpa.getTotalPages();

        number = pageFromJpa.getNumber();

        totalElements = pageFromJpa.getTotalElements();

        size = pageFromJpa.getSize();

        numberOfElements = pageFromJpa.getNumberOfElements();

        content = pageFromJpa.getContent();

        isHasContent = pageFromJpa.hasContent();

        first = pageFromJpa.isFirst();

        last = pageFromJpa.isLast();

        isHasNext = pageFromJpa.hasNext();

        isHasPrevious = pageFromJpa.hasPrevious();

        claNavigatePageNums();

    }

    private void claNavigatePageNums() {
        int[] navigatePageNums;
        int totalPages = getTotalPages();
        int num = getNumber();
        //当总页数小于或等于导航页码数时
        if (totalPages <= navigatePages) {
            navigatePageNums = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                navigatePageNums[i] = i + 1;
            }
        } else { //当总页数大于导航页码数时
            navigatePageNums = new int[navigatePages];
            int startNum = num - navigatePages / 2;
            int endNum = num + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            } else if (endNum > totalPages) {
                endNum = totalPages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    navigatePageNums[i] = endNum--;
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    navigatePageNums[i] = startNum++;
                }
            }
        }
        this.navigatePageNums = navigatePageNums;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isHasContent() {
        return isHasContent;
    }

    public void setHasContent(boolean isHasContent) {
        this.isHasContent = isHasContent;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasNext() {
        return isHasNext;
    }

    public void setHasNext(boolean isHasNext) {
        this.isHasNext = isHasNext;
    }

    public boolean isHasPrevious() {
        return isHasPrevious;
    }

    public void setHasPrevious(boolean isHasPrevious) {
        this.isHasPrevious = isHasPrevious;
    }

    public int[] getnavigatePageNums() {
        return navigatePageNums;
    }

    public void setnavigatePageNums(int[] navigatePageNums) {
        this.navigatePageNums = navigatePageNums;
    }

}
