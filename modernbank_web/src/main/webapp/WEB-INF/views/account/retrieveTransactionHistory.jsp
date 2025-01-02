<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<br />
	<h2>
		<span>입출금이력조회</span>
	</h2>
	<form id="form1" onsubmit="return false;">
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:16%;" />
				<col style="width:84%;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="width100">계좌번호</th>
					<td>
						<input class="txt" id="acntNo" type="text" />
						<input id="btnSearch" type="button" value="조회" class="search_btn"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<br />
	  계좌 정보
	<table class="global_table" cellpadding="0" cellspacing="0">
		<colgroup>
			<col style="width:16%;" />
			<col style="width:17%;" />
			<col style="width:16%;" />
			<col style="width:17%;" />
			<col style="width:16%;" />
			<col style="width:17%;" />
		</colgroup>
		<tbody>
			<tr>
				<th class="width100">계좌명</th>
				<td name="acntNm"></td>
				<th class="width100">계좌번호</th>
				<td name="acntNo"></td>
				<th class="width100">계좌잔액</th>
				<td name="acntBlnc"></td>
			</tr>
			<tr>
				<th class="width100">고객ID</th>
				<td name="cstmId"></td>
				<th class="width100">고객명</th>
				<td name="cstmNm"></td>
				<th class="width100">신규일시</th>
				<td name="newDtm"></td>
			</tr>
		</tbody>
	</table>
	<br/>
	입출금내역
	<table class="search_results" cellpadding="0" cellspacing="0">
		<colgroup>
			<col style="width:14%;" />
			<col style="width:15%;" />
			<col style="width:14%;" />
			<col style="width:15%;" />
			<col style="width:14%;" />
			<col style="width:15%;" />
			<col style="width:13%;" />
		</colgroup>
		<tbody id="trnsHst">
			<tr>
				<th class="width100">거래일자</th>
				<th class="width100">거래시간</th>
				<th class="width100">출금(원)</th>
				<th class="width100">입금(원)</th>
				<th class="width100">잔액(원)</th>
				<th class="width100">거래점</th>
				<th class="width100">상태</th>
			</tr>
		</tbody>
	</table>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/account/retrieveTransactionHistory.js"></script>
