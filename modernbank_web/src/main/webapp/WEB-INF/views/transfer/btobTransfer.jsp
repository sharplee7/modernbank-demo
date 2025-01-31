<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="container">
	<div class="transfer_container">
		<br />
		<h2>
			<span>타행 이체</span>
		</h2>
		출금정보
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
						<th class="width100">출금계좌번호</th>
						<td colspan="5">
							<input class="txt" id="wthdAcntNo" type="text" />
							<input id="btnSearch" type="button" value="조회" class="search_btn"/>
						</td>
					</tr>
					<tr>
						<th class="width100">계좌명</th>
						<td name="acntNm"></td> 
						<th class="width100">계좌잔액</th>
						<td name="acntBlnc"><span id="acntBlnc"></span> 원</td>
					</tr>
					<tr>
						<th class="width100">고객ID</th>
						<td name="cstmId"></td>
						<th class="width100">고객명</th>
						<td name="cstmNm"></td>
					</tr>
					<tr>
						<th class="width100">잔여 1일 이체한도</th>
						<td name="oneDyTrnfLmt"><span id="oneDyTrnfLmt"></span> 원</td> 
						<th class="width100">1회 이체한도</th>
						<td name="oneTmTrnfLmt"><span id="oneTmTrnfLmt"></span> 원</td>
					</tr>
				</tbody>
			</table>
		</form>
		<br />
		 입금정보
		<table class="global_table" cellpadding="0" cellspacing="0">
			<colgroup>
				<col style="width:20%;" />
				<col style="width:30%;" />
				<col style="width:20%;" />
				<col style="width:30%;" />
			</colgroup>
			<tbody>
				<tr>
					<th class="width100">입금계좌은행</th>
					<td colspan="3">
						<select id="dpstBank" name="acntNm">
							<option value = "춘 은행">춘 은행</option>
						</select>
					</td>
				</tr>
				<tr>
					<th class="width100">입금계좌번호</th>
					<td><input class="txt" id="dpstAcntNo" type="text"/></td>
					<th class="width100">이체금액</th>
					<td><input class="txt" id="trnfAmt" type="text"/></td>
				</tr>
				<tr>
					<th class="width100">받는통장메모</th>
					<td colspan=><input class="txt" id="rcvMm" type="text"/> 7자 이내 입력</td>
					
					<th class="width100">내통장메모</th>
					<td colspan=><input class="txt" id="sndMm" type="text"/> 7자 이내 입력</td>
				</tr>
			</tbody>
		</table>
		<div class="g_tablebtn">
			<select id="stsCd" name="stsCd" style="width:100px;margin-bottom:5px">
				<option value = "2">실패</option>
				<option value = "3">성공</option>
			</select>
			<input id="btnRequest" type="button" value="이체요청" class="excute_btn" hidden="hidden" />
		</div>
	</div>
</div>
<script type="text/javascript"
		src="<%=request.getContextPath()%>/resource/js/transfer/btobTransfer.js"></script>

