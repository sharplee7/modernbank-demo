<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<br />
	<h2>
		<span>이체이력조회</span>
	</h2>
	<form id="form1" onsubmit="return false;">
		<table class="global_table" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<th class="width100">고객번호</th>
					<td colspan="5">
						<input class="txt" id="cstmId" type="text" />
						<input id="btnSearch" type="button" value="조회" class="search_btn"/>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
	<br />
	이체내역
	<table class="search_results" cellpadding="0" cellspacing="0" id="transferHistoryList">
			<tbody>
				<tr>
					<th class="width135">이체구분</th>
					<th class="width135">거래일시</th>
					<th class="width135">출금계좌</th>
					<th class="width135">입금계좌</th>
					<th class="width135">받는 분</th>
					<th class="width135">이체금액</th>
					<th class="width135">받는 통장 메모</th>
					<th class="width135">내 통장 메모</th>
					<th class="width135">상태</th>
				</tr>
			</tbody>
		</table>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/transfer/transferHistory.js"></script>

