<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="container">
	<br />
	<h2>
		<span>계좌 개설</span>
	</h2>

	<form id="form1" onsubmit="return false;">
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:20%;" />
				<col style="width:30%;" />
				<col style="width:20%;" />
				<col style="width:30%;" />
			</colgroup>
			<tbody>
				<tr> 
					<th class="width135">계좌명</th>
					<td>
						<select id="acntNm" name="acntNm">
							<option value = "혜자 계좌">혜자 계좌</option>
							<option value = "안 하면 손해 계좌">안 하면 손해 계좌</option>
						</select>
					</td>
					<th class="width135">계좌번호</th>
					<td><input class="txt" id="acntNo" name="acntNo" type="text" /></td>
				</tr>
				<tr>
					<th class="width135">고객ID</th>
					<td><input class="txt" id="cstmId" name="cstmId" type="text" /></td>
					<th class="width135">입금 금액</th>
					<td><input class="txt" id="dpstAmt" name="dpstAmt" type="text" /></td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" id="trnsBrnch" value="역삼본점"/>
		<div class="g_tablebtn">
			<input id="btnSearch" type="button" value="등록" class="g_btn_bg" />
		</div>
	</form>
	<br />
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/account/createAccount.js"></script>