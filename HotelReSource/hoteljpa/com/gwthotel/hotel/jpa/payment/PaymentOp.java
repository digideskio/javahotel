/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.gwthotel.hotel.jpa.payment;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.gwthotel.hotel.jpa.entities.EBillPayment;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.payment.PaymentBill;
import com.jython.jpautil.JpaUtils;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;

public class PaymentOp implements IPaymentBillOp {

	private final ITransactionContextFactory eFactory;

	@Inject
	public PaymentOp(ITransactionContextFactory eFactory) {
		this.eFactory = eFactory;
	}

	private abstract class doTransaction extends JpaTransaction {

		protected final OObjectId hotel;
		protected final String billName;

		doTransaction(OObjectId hotel, String billName) {
			super(eFactory);
			this.hotel = hotel;
			this.billName = billName;
		}

		ECustomerBill findBill(EntityManager em) {
			return JpaUtils.getElemE(em, hotel, "findOneBill", billName);
		}

		PaymentBill toB(EBillPayment e) {
			PaymentBill b = new PaymentBill();
			b.setBillName(e.getCustomerBill().getName());
			b.setDateOfPayment(e.getDateOfPayment());
			b.setDescription(e.getDescription());
			b.setPaymentTotal(e.getPaymentTotal());
			b.setPaymentMethod(e.getPaymentMethod());
			b.setId(e.getId());
			b.setAdvancepayment(e.isAdvancepayment());
			return b;
		}
	}

	private class GetPaymentsForBill extends doTransaction {

		private List<PaymentBill> pList = new ArrayList<PaymentBill>();

		GetPaymentsForBill(OObjectId hotel, String billName) {
			super(hotel, billName);
		}

		@Override
		protected void dosth(EntityManager em) {
			Query q = JpaUtils.createObjectQuery(em, findBill(em), "findAllPaymentsForBill");
			// q.setParameter(2, findBill(em));
			List<EBillPayment> rList = q.getResultList();
			for (EBillPayment e : rList)
				pList.add(toB(e));
		}

	}

	@Override
	public List<PaymentBill> getPaymentsForBill(OObjectId hotel, String billName) {
		GetPaymentsForBill tran = new GetPaymentsForBill(hotel, billName);
		tran.executeTran();
		return tran.pList;
	}

	private class AddPaymentToBill extends doTransaction {

		private final PaymentBill p;
		private PaymentBill res;

		AddPaymentToBill(OObjectId hotel, String billName, PaymentBill p) {
			super(hotel, billName);
			this.p = p;
		}

		@Override
		protected void dosth(EntityManager em) {
			ECustomerBill b = findBill(em);
			EBillPayment e = new EBillPayment();
			// only creation date
			BUtil.setCreateModif(hotel.getUserName(), e, true);
			e.setCustomerBill(b);
			e.setDateOfPayment(p.getDateOfPayment());
			e.setPaymentMethod(p.getPaymentMethod());
			e.setPaymentTotal(p.getPaymentTotal());
			e.setDescription(p.getDescription());
			e.setAdvancepayment(p.isAdvancepayment());
			em.persist(e);
			EBillPayment addE = em.find(EBillPayment.class, e.getId());
			res = toB(addE);
		}

	}

	@Override
	public PaymentBill addPaymentForBill(OObjectId hotel, String billName, PaymentBill payment) {
		AddPaymentToBill trans = new AddPaymentToBill(hotel, billName, payment);
		trans.executeTran();
		return trans.res;
	}

	private class RemovePaymentForBill extends doTransaction {

		private final Long paymentId;

		RemovePaymentForBill(OObjectId hotel, String billName, Long paymentId) {
			super(hotel, billName);
			this.paymentId = paymentId;
		}

		@Override
		protected void dosth(EntityManager em) {
			// only as additional test
			// findBill(em);
			EBillPayment e = em.find(EBillPayment.class, paymentId);
			em.remove(e);
		}

	}

	@Override
	public void removePaymentForBill(OObjectId hotel, String billName, Long paymentId) {
		RemovePaymentForBill trans = new RemovePaymentForBill(hotel, billName, paymentId);
		trans.executeTran();
	}

}
