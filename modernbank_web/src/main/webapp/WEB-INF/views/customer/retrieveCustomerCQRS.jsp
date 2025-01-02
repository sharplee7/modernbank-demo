<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container">
	<br />
	<h2>
		<span>고객 조회</span>
	</h2>
    
	<form id="form1" onsubmit="return false;">
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:16%;" />
				<col style="width:84%;" />
			</colgroup>
			<tr>
				<th class="width135">고객ID</th>
				<td>
					<input class="txt" id="cstmId" name="cstmId" type="text" value="" />
					<input id="btnSearch" type="button" value="조회" class="search_btn"/>
				</td>
			</tr>
		</table>
	</form>			
	<br />

   	<h3>고객 정보</h3>  
	<table class="global_table" cellpadding="0" cellspacing="0">
		<colgroup>
			<col style="width:16%;" />
			<col style="width:17%;" />
			<col style="width:16%;" />
			<col style="width:17%;" />
			<col style="width:16%;" />
			<col style="width:18%;" />
		</colgroup>
		<tbody>
			<tr>
				<th class="width100">고객ID</th>
				<td name="cstmId"></td>
				<th class="width100">이름</th>
				<td name="cstmNm"></td>
				<th class="width100">성별</th>
				<td name="cstmGnd"></td>
			</tr>
			<tr>
				<th class="width100">나이</th>
				<td name="cstmAge"></td>
				<th class="width100">주소</th>
				<td name="cstmAdr"></td>
				<th class="width100">전화번호</th>
				<td name="cstmPn"></td>
			</tr>
		</tbody>
	</table>
	<br />
	
	<h3>이체 한도 정보</h3>
	<table width="1200" border="3" cellpadding="0" cellspacing="1"	class="search_results" id="table">
		<colgroup>
			<col style="width:25%;" />
			<col style="width:25%;" />
			<col style="width:25%;" />
			<col style="width:25%;" />
		</colgroup>
		<tbody>
			<tr>
				<th>1일 이체 한도</th>
				<td name="oneDyTrnfLmt"></td>
				<th>1회 이체 한도</th>
				<td name="oneTmTrnfLmt"></td>
			</tr>
		</tbody>
	</table>
	<br />
	
	<h3>계좌 정보</h3>
	<table width="1200" border="3" cellpadding="0" cellspacing="1"	class="search_results" id="table">
		<colgroup>
			<col style="width:25%;" />
			<col style="width:25%;" />
			<col style="width:25%;" />
			<col style="width:25%;" />
		</colgroup>
		<tbody id="accountList">
			<tr>
				<th>계좌명</th>
				<th>계좌번호</th>
				<th>계좌잔액</th>
				<th>신규일시</th>
			</tr>
		</tbody>
	</table>

</div>

<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/customer/retrieveCustomerCQRS.js"></script>