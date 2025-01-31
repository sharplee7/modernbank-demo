<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

    <div id="header">
        <div>
            <div class="logo">
                <img src="<%=request.getContextPath()%>/resource/tiles/images/logo.png"/>
            </div>
            <div>
            	<input type="hidden" id="contextPath" value = "<%=request.getContextPath()%>"/>
            	<input type="hidden" id="cqrsEnable" value = "<spring:eval expression="@environment.getProperty('cqrs-enable')"/>"/>
            	<input type="hidden" id="transferApiUrl" value = "<spring:eval expression="@environment.getProperty('transfer.api.url')"/>"/>
            	<input type="hidden" id="accountApiUrl" value = "<spring:eval expression="@environment.getProperty('account.api.url')"/>"/>
            	<input type="hidden" id="customerApiUrl" value = "<spring:eval expression="@environment.getProperty('customer.api.url')"/>"/>
            	<input type="hidden" id="cqrsApiUrl" value = "<spring:eval expression="@environment.getProperty('cqrs.api.url')"/>"/>
            </div>
        </div>
    </div>

    
    <div id="menu" class="container">
    	<li id="catalogMenu"><a href="<%=request.getContextPath()%>/main">Catalog</a></li>
    	<li id="customerMenu">
    		<a href="<%=request.getContextPath()%>/main">고객 관리</a>
    		<ul>
    			<li><a href="<%=request.getContextPath()%>/customer/view/createCustomer">고객 등록</a></li>
    			<li><a href="<%=request.getContextPath()%>/customer/view/retrieveCustomer">고객 조회</a></li>
    			<li class="cqrsTab"><a href="<%=request.getContextPath()%>/customer/view/retrieveCustomerCQRS">고객 조회(CQRS)</a></li>
    	
    		</ul>
    	</li>
    	<li id="accountMenu">
    		<a href="<%=request.getContextPath()%>/main">계좌 관리</a>
    		<ul>
    			<li><a href="<%=request.getContextPath()%>/account/view/createAccount">계좌 개설</a></li>
    			<li><a href="<%=request.getContextPath()%>/account/view/deposit">입금</a></li>
    			<li><a href="<%=request.getContextPath()%>/account/view/withdraw">출금</a></li>
    			<li><a href="<%=request.getContextPath()%>/account/view/retrieveTransactionHistory">입출금 이력 조회</a></li>
    		</ul>
    	</li>
    	<li id="transferMenu">
    		<a href="<%=request.getContextPath()%>/main">계좌 이체</a>
    		<ul>
    			<li><a href="<%=request.getContextPath()%>/transfer/view/transfer">당행 이체</a></li>
    			<li><a href="<%=request.getContextPath()%>/transfer/view/btobTransfer">타행 이체</a></li>
    			<li><a href="<%=request.getContextPath()%>/transfer/view/retrieveTransferHistory">이체 이력 조회</a></li>
    		</ul>
    	</li>
    </div>
<script>
$(function() {
	var cqrsEnable = $('#cqrsEnable').val();
	if(cqrsEnable == "true"){
		$(".cqrsTab").show();
	}else{
		$(".cqrsTab").hide();
	}
});
</script>